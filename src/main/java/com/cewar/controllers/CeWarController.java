package com.cewar.controllers;

import java.io.FileNotFoundException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cewar.model.dtos.CardDto;
import com.cewar.model.dtos.RegisterDto;
import com.cewar.model.entity.Card;
import com.cewar.model.entity.User;
import com.cewar.model.entity.UserCard;
import com.cewar.model.entity.UserDeck;
import com.cewar.model.session.FilteredCardCollection;
import com.cewar.model.session.PackGenerator;
import com.cewar.model.session.PackGenerator.*;
import com.cewar.services.CardService;
import com.cewar.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

/**
 * Main webpage controller. Processes web requests
 * 
 * GET Mappings
 * /            home.html           Home Page
 * /card        database.html       Sends database of all cards
 * /write       writeDatabase.html  Sends database of cards, with extra options to add cards to one's inventory
 * /pack        cardPack.html       Pack generator
 * /login       login.html
 * /register    register.html
 * /test_rest                       Test page for REST responses
 * 
 * POST Mappings
 * /pack        Handles pack generation and verification
 * /register    Handles registration data
 */
@Controller
public class CeWarController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Sends home.html when a user navigates to the home page (index)
     * 
     * <p> <strong>Authority:</strong> NONE
     */
    @GetMapping("")
    public String index() {
        return "home";
    }

    /**
     * Directs to the login page
     * 
     * <p> <strong>Authority:</strong> NONE
     */
    @GetMapping("/login")
    public String getLogin(Model model, HttpServletResponse reponse) {
        return "login";
    }

    /**
     * Directs to the user signup page
     * 
     * <p> <strong>Authority:</strong> NONE
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        RegisterDto regDto = new RegisterDto();
        model.addAttribute("user", regDto);
        return "register";
    }

    /**
     * Handles POST requests of new user sign ups
     * 
     * @param regDto a valid {@link RegisterDto}
     */
    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid RegisterDto regDto, Model model) {
        Boolean validInfo = true;
        if (!regDto.getEmail().matches("^\\S+@\\S+\\.\\S+$") // Regex to parse email
            || (userService.getByEmail(regDto.getEmail()) != null) ) {
            model.addAttribute("error_email", true);
            validInfo = false;
        }
        if (userService.getByUsernameIgnoreCase(regDto.getUsername()) != null) {
            model.addAttribute("error_username_exists", true);
            validInfo = false;
        }
        if (regDto.getUsername().contains("[^a-zA-Z0-9-_.]")) { // Check for any characters that are not allowed
            model.addAttribute("error_username_text", true);
            validInfo = false;
        }
        if (!regDto.getPassword().equals(regDto.getPasswordConfirm())) {
            model.addAttribute("error_password", true);
            validInfo = false;
        }

        if (validInfo) {
            User user = new User(regDto.getUsername(), passwordEncoder.encode(regDto.getPassword()), regDto.getEmail());
            userService.save(user);

            model.addAttribute("post_register", true);
            return "redirect:"; // Redirect to home page
        } else {
            return "register";
        }
    }

    /**
     * Gets a searchable database of all known cards
     * 
     * <p> <strong>Authority:</strong> NONE
     */
    @GetMapping("/card")
    public String getCardIndex(Model model) {
        FilteredCardCollection fcc = new FilteredCardCollection(cardService.getAll(), FilteredCardCollection.SortType.ARCHETYPE);
        model.addAttribute("cardDataArr", fcc.toArray());
        return "database";
    }

    /**
     * Sends pack generator page. 
     * 
     * <p> <strong>Authority:</strong> USER
     */
    @GetMapping("/pack")
    public String cardPack(Model model) {
        return "cardPack";
    }

    /**
     * Handles a POST request from the /pack subpage and generates a card pack
     * 
     * <p> <strong>Authority:</strong> USER
     * 
     * <p>
     * FIXME users will get scammed if a roll is invalid and can't find a card to choose
     * 
     * @param packType
     * @param model
     * @return
     */
    @PostMapping("/pack")
    public String postCardPack(@ModelAttribute(name = "packTypeDropdown") String packType, Model model) {

        // Get user information
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        // Ensure user has enough points to make this purchase
        if ((packType.equals(PackGenerator.Pack.RARE.toString())  && user.getInfo().getPoints() < 20)
            || !packType.equals(PackGenerator.Pack.RARE.toString()) && user.getInfo().getPoints() < 10) {
            // Add an attribute to tell the user that they don't have enough points and exit
            model.addAttribute("error_points", true);
            return "cardPack";
        }

        // Collect generated cards, given POST request parameters
        List<CardDto> packOutput = new ArrayList<>();
        try {
            packOutput = PackGenerator.generate(Pack.valueOf(packType), cardService.getAll());
        } catch (UnexpectedException e) { // These should never happen
            // If packtype is invalid. Should not happen, because users pick from a dropdown
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // If card database could not be reached, or Card Back is not in it for some reason
            e.printStackTrace();
        }

        // Subtract points from user
        if (packType.equals(Pack.RARE.toString())) {
            user.getInfo().setPoints(user.getInfo().getPoints() - 20);
        } else {
            user.getInfo().setPoints(user.getInfo().getPoints() - 10);
        }

        Collection<UserCard> cardList = userService.addCards(user, packOutput);
        if (cardList == null) {
            /* Adding cards failed to happen for some reason
                Abort and return nothing. Nothing will be saved. */ 
            // TODO add feedback to User that the pack failed to generate
            return "cardPack";
        }

        ArrayList<Card> output = new ArrayList<>();
        for (UserCard uCard : cardList) {
            output.add(uCard.asCard());
        }

        // Add String representation of pack output to page attributes as packOutput
        model.addAttribute("packOutput", output.toArray());

        return "cardPack";
    }

    /**
     * Maps to a database accessible by users with the "WRITE" authority
     * 
     * <p> <strong>Authority:</strong> WRITE
     */
    @GetMapping("/write")
    public String getWritePage(Model model) {
        FilteredCardCollection fcc = new FilteredCardCollection(cardService.getAll(), FilteredCardCollection.SortType.ARCHETYPE);
        model.addAttribute("cardDataArr", fcc.toArray());
        return "writeDatabase";
    }

    /**
     * Tests REST templates using Spring
     * 
     */
    @GetMapping("/test_rest")
    @ResponseBody
    public User testRest(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost/api/users"; // Points to admin user

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");

        HttpEntity<User> request = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(resourceUrl + "/1", HttpMethod.GET, request, User.class);

        return response.getBody();
    }

    @GetMapping("/test_stuff_1")
    public void testStuff1(Model model) {
        User user = userService.getByUsername("Zeter");
        Collection<UserDeck> decks = user.getDecks();

        UserDeck newDeck = new UserDeck("test", user);
        decks.add(newDeck);
        userService.save(user);
    }

    @GetMapping("/test_stuff_2")
    public void testStuff2(Model model) {
        User user = userService.getByUsername("Zeter");
        Collection<UserDeck> decks = user.getDecks();

        UserDeck firstDeck = decks.iterator().next();

        Collection<UserCard> deckContents = firstDeck.getContents();

        deckContents.add(user.getInventory().iterator().next());

        userService.save(user);
    }

    @GetMapping("/rules")
    public String getRules() {
        return "rules";
    }
}
