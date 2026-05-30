package com.cewar.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cewar.model.dtos.DeckDto;
import com.cewar.model.entity.User;
import com.cewar.model.entity.UserCard;
import com.cewar.model.entity.UserDeck;
import com.cewar.model.entity.UserInfo;
import com.cewar.model.session.FilteredCardCollection;
import com.cewar.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * Controller for user accounts
 * 
 * <p> <strong>Authority:</strong> USER
 * 
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private final AdminController adminController;

    @Autowired
    private UserService userService;

    AccountController(AdminController adminController) {
        this.adminController = adminController;
    }

    /**
     * Sends account.html template for the current user
     */
    @GetMapping("")
    public String getUserAccount(Model model, HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("user", getPublicUserInfo(username));
        return "account";
    }

    /**
     * Returns a database of all cards a user owns
     */
    @GetMapping("/inventory")
    public String getCardIndex(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        FilteredCardCollection fcc = new FilteredCardCollection(userService.getUserInventoryAsCards(username), FilteredCardCollection.SortType.ARCHETYPE);
        model.addAttribute("cardDataArr", fcc.toArray());
        return "inventory";
    }

    /**
     * Helper method to get only public information about a user.
     * 
     * @param username - username to look up for
     * @return
     */
    private UserInfo getPublicUserInfo(String username) {
       return userService.getByUsername(username).getInfo();

       // REVIEW html may not be able to access methods inside UserInfo to find username and number of cards
    }

    /**
     * Directs to either the deck manager or the deck editor, depending on whether the deck ID is provided
     * 
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/deck")
    public String getDeck(@RequestParam(required = false) Long id, Model model) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (id == null) {
            // If deck ID is not provided, return the deck manager for this user

            ArrayList<DeckDto> decks = new ArrayList<>();
            for (UserDeck deck : user.getDecks()) {
                decks.add(makeDeckDto(deck));
            };

            model.addAttribute("deckList", decks.toArray());
            return "deckManager";
        } else {
            // If deck ID is provided, return the deck editor for that ID
            Collection<UserDeck> deckList = user.getDecks();
            for (UserDeck deck : deckList) {
                if (deck.getId() == id) {
                    // Deck ID requested belongs to this user

                    // Get a list of all cards Player owns
                    FilteredCardCollection fcc = new FilteredCardCollection(userService.getUserInventoryAsCards(user), FilteredCardCollection.SortType.ARCHETYPE);
                    model.addAttribute("cardDataArr", fcc.toArray());

                    // Create new DeckDto and add it as an attribute
                    model.addAttribute("deckDto", makeDeckDto(deck));
                    return "deckEditor";
                }
            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deck ID was either not found or not accessible by this user");
        }
    }

    /**
     * Helper method to create and populate a DTO for a deck
     * 
     * @see DeckDto
     * 
     * @param deck
     * @return
     */
    private DeckDto makeDeckDto(UserDeck deck) {
        DeckDto output = new DeckDto();
        output.setId(deck.getId());
        output.setName(deck.getName());
        List<String> cardIds = new ArrayList<>();
        for (UserCard card : deck.getContents()) {
            cardIds.add(card.getId().toString());
        }
        output.setCardIds(cardIds);
        return output;
    }

    @PostMapping("/deck")
    public ResponseEntity<?> postDeck(@RequestBody @Valid DeckDto deckDto, Model model) {
        // Validate Dto, then apply changes
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Collection<UserDeck> allDecks = user.getDecks();

        // TODO also ensure that there is exactly 1 God card in the deck
        for (UserDeck deck : allDecks) {
            if (deck.getId() == deckDto.getId()) {
                // Deck ID in DTO is for a deck owned by this user

                // Check that all cards in DTO belong to the user and there are no duplicates
                boolean deckValid = true;
                ArrayList<UserCard> allCards = new ArrayList<>(user.getInventory());
                ArrayList<String> deckCardIds = new ArrayList<>(deckDto.getCardIds());
                ArrayList<UserCard> newDeckContents = new ArrayList<>();

                validateLoop:
                for (int i = 0; i < deckCardIds.size(); i++) {
                    // Make sure this card's ID does not repeat
                    for (int j = i + 1; j < deckCardIds.size(); j++) {
                        if (deckCardIds.get(i).equals(deckCardIds.get(j))) {
                            deckValid = false;
                            break validateLoop;
                        }
                    }
                    // Check if this card belongs to this User
                    for (UserCard uCard : allCards) {
                        if (uCard.getId().toString().equals(deckCardIds.get(i))) {
                            // Card ID is found in User's inventory
                            newDeckContents.add(uCard);
                            continue validateLoop; // Jumps to next value of i
                        }
                    }
                    // If loop exits with no matches and gets here, then the card does not belong to this user
                    deckValid = false;
                    break validateLoop;
                }
                if (deckValid) {
                    // Deck ID is valid, cards inside are valid.
                    // newDeckContents should have all the cards if everything was valid
                    deck.setContents(newDeckContents);
                    deck.setName(deckDto.getName());
                    try {
                        userService.save(user);
                        return ResponseEntity.ok().build();
                    } catch (Exception e) {
                        // In case deck failed to save
                        System.out.println("Error while saving deck: " + e.toString());
                        return ResponseEntity.internalServerError().build();
                    }
                } else {
                    // Break here since the DeckDTO is invalid
                    break;
                }
            }
        }
        // Deck ID in DTO is invalid or doesn't correspond to a deck owned by this user,
        // or cards in deck are in an illegal state (duplicates or not owned by user)
        // This shouldn't normally happen
        // Send 400 Bad Request to User
        return ResponseEntity.badRequest().build();
    }

    /**
     * Creates a new, empty deck
     * 
     * Defaults to "Untitled" or "Untitled-##" if it already exists.
     */
    @PostMapping("/newdeck")
    public ResponseEntity<?> newDeck(Model model) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        // Attempt to make a new deck
        int newNameAttempt = 0;
        String newName = "";
        while(true) {
            // Track if "Untitled" will be a unique name
            boolean nameExists = false;
            for (UserDeck deck : user.getDecks()) {
                if (newNameAttempt == 0) {
                    if (deck.getName().equals("Untitled")) {
                        nameExists = true;
                        break;
                    }
                } else {
                    if (deck.getName().equals("Untitled-" + newNameAttempt)) {
                        nameExists = true;
                        break;
                    }
                }
            }

            // Use "Untitled" if it is unique, or repeat and do "Untitled-1" and so on
            if (nameExists) {
                newNameAttempt++;
            } else {
                if (newNameAttempt == 0) {
                    newName = "Untitled";
                } else {
                    newName = "Untitled-" + newNameAttempt;
                }
                break;
            }
        }

        // Create a new deck using the new name
        user.getDecks().add(new UserDeck(newName, user));
        userService.save(user);
        String deckId = "";
        for (UserDeck deck : user.getDecks()) {
            if (deck.getName().equals(newName)) {
                deckId = deck.getId().toString();
            }
        }
        
        // Ensure deck actually got created
        if (deckId.equals("")) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(deckId);
    }
}
