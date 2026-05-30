package com.cewar.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cewar.model.dtos.CardDto;
import com.cewar.model.entity.User;
import com.cewar.model.entity.UserCard;
import com.cewar.model.entity.UserDeck;
import com.cewar.repositories.UserCardRepository;
import com.cewar.repositories.UserDeckRepository;
import com.cewar.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service to access and manage {@link com.cewar.model.entity.User Users}, as well as 
 * {@link com.cewar.model.entity.UserCard UserCards} and {@link com.cewar.model.entity.UserDeck UserDecks}
 */
@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserCardRepository cardRepo;

    @Autowired
    private UserDeckRepository deckRepo;

    /* SECTION User Repository Methods */

    /* Required for DaoAuthenticationProvider to work, would just be "getUserByUsername" if possible */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> result = userRepo.findByUsernameIgnoreCase(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Username not found with username: " + username);
        }
        return result.get();
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        return loadUserByUsername(username);
    }

    public User getByUsernameIgnoreCase(String username) throws UsernameNotFoundException {
        Optional<User> result = userRepo.findByUsernameIgnoreCase(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Username not found with username when ignoring case: " + username);
        }
        return result.get();
    }

    public User getByEmail(String email) throws EntityNotFoundException {
        Optional<User> result = userRepo.findByEmail(email);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("User not found with email " + email);
        }
        return result.get();
    }

    /* SECTION CRUD Operations from CrudRepository */

    public User getById(long id) throws EntityNotFoundException {
        Optional<User> result = userRepo.findById(id);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("User not found with ID " + id);
        }
        return result.get();
    }

    public Collection<User> getAll() {
        return userRepo.findAll();
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public Collection<User> saveAll(Iterable<User> users) {
        return userRepo.saveAll(users);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    /* !SECTION */
    /* !SECTION */

    /* SECTION UserCard Repository Methods */

    public UserCard getCardById(long id) throws EntityNotFoundException {
        Optional<UserCard> result = cardRepo.findById(id);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("Deck not found with ID " + id);
        }
        return result.get();
    }

    /**
     * Creates and adds a card to a user's inventory
     * 
     * @param userId - the User ID of the owner
     * @param cardData - the data used to create a new UserCard
     * @return the new UserCard, if successful. If not, null will be returned and nothing will be saved.
     */
    public UserCard addCard(long userId, CardDto cardData) throws EntityNotFoundException {
        Optional<User> result = userRepo.findById(userId);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("User not found with ID " + userId);
        }
        User owner = result.get();

        UserCard newCard = new UserCard(owner, cardData);
        
        // Try to add card to inventory
        if (owner.getInventory().add(newCard)) {
            userRepo.save(owner);
            return newCard;
        } else {
            // Something went wrong, and the card could not be added properly
            return null;
        }
    }

    /**
     * Creates and adds several cards to a user's inventory
     * 
     * @param userId - the User ID of the owner
     * @param cardDataCollection - a collection of data used to create new UserCards
     * @return A collection of UserCards if successful. If not, null will be returned and nothing will be saved.
     */
    public Collection<UserCard> addCards(long userId, Collection<CardDto> cardDataCollection) throws EntityNotFoundException {
        Optional<User> result = userRepo.findById(userId);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("User not found with ID " + userId);
        }
        User owner = result.get();

        ArrayList<UserCard> output = new ArrayList<>();

        for (CardDto cardData : cardDataCollection) {
            UserCard newCard = new UserCard(owner, cardData);
            
            // Try to add card to inventory
            if (owner.getInventory().add(newCard)) {
                output.add(newCard);
            } else {
                // Something went wrong, and the card could not be added properly
                return null;
            }
        }
        // All cards added properly
        userRepo.save(owner);
        return output;
    }

    /* !SECTION */

    /* SECTION UserDeck Repository Methods */

    public UserDeck getDeckById(long id) throws EntityNotFoundException {
        Optional<UserDeck> result = deckRepo.findById(id);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("Deck not found with ID " + id);
        }
        return result.get();
    }

    /* !SECTION */

}