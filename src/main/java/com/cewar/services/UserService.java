package com.cewar.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cewar.model.dtos.CardDto;
import com.cewar.model.entity.Card;
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

    /**
     * Gets all UserCards owned by a user
     * 
     * @param user - the user
     * @return Collection of UserCards owned by a user
     */
    public Collection<UserCard> getUserInventory(User user) {
        return user.getInventory();
    }

    /**
     * Gets all UserCards owned by a user
     * 
     * @param username - the username of the user
     * @return Collection of UserCards owned by a user
     * @throws UsernameNotFoundException if no user exists with that username
     * 
     * @see #getUserInventory(User)
     */
    public Collection<UserCard> getUserInventory(String username) throws UsernameNotFoundException {
        Optional<User> result = userRepo.findByUsername(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username " + username);
        }
        return result.get().getInventory();
    }

    /**
     * Gets all cards owned by a user as {@link Card}s
     * 
     * @param user - the user
     * @return Collection of Cards representing cards owned by the user
     */
    public Collection<Card> getUserInventoryAsCards(User user){
        Collection<UserCard> inventory = getUserInventory(user);
        Collection<Card> output = new ArrayList<>();
        for (UserCard card : inventory) {
            output.add(card.asCard());
        }
        return output;
    }

    /**
     * Gets all cards owned by a user as {@link Card}s
     * 
     * @param username - the username of the user
     * @return Collection of Cards representing cards owned by the user
     * @throws UsernameNotFoundException if no user exists with that username
     * 
     * @see #getUserInventoryAsCards(User)
     */
    public Collection<Card> getUserInventoryAsCards(String username) throws UsernameNotFoundException {
        Optional<User> result = userRepo.findByUsername(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username " + username);
        }
        return getUserInventoryAsCards(result.get());
    }

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
     * Creates and adds a card to a user's inventory. Then saves the new card and the user.
     * 
     * @param user - the user to add a card to
     * @param cardData - the data used to create a new UserCard
     * @return the new UserCard, if successful. If not, null will be returned and nothing will be saved.
     */
    public UserCard addCard(User user, CardDto cardData) {
        UserCard newCard = new UserCard(user, cardData);
        
        // Try to add card to inventory
        if (user.getInventory().add(newCard)) {
            userRepo.save(user);
            return saveCard(newCard);
        } else {
            // Something went wrong, and the card could not be added properly
            return null;
        }
    }

    /**
     * Creates and adds a card to a user's inventory. Then saves the new card and the user.
     * 
     * @param username - the username of the owner
     * @param cardData - the data used to create a new UserCard
     * @return the new UserCard, if successful. If not, null will be returned and nothing will be saved.
     * @throws UsernameNotFoundException if no user exists with that username
     * 
     * @see #addCard(User, CardDto)
     */
    public UserCard addCard(String username, CardDto cardData) throws UsernameNotFoundException {
        Optional<User> result = userRepo.findByUsername(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username " + username);
        }
        return addCard(result.get(), cardData);
    }

    /**
     * Creates and adds several cards to a user's inventory. Then saves the new cards and the user.
     * 
     * @param user - the user to add cards to
     * @param cardDataCollection - a collection of data used to create new UserCards
     * @return A collection of UserCards if successful. If not, null will be returned and nothing will be saved.
     */
    public Collection<UserCard> addCards(User user, Collection<CardDto> cardDataCollection) {
        ArrayList<UserCard> output = new ArrayList<>();

        for (CardDto cardData : cardDataCollection) {
            UserCard newCard = new UserCard(user, cardData);
            
            // Try to add card to inventory
            if (user.getInventory().add(newCard)) {
                output.add(newCard);
            } else {
                // Something went wrong, and the card could not be added properly
                return null;
            }
        }
        // All cards added properly
        userRepo.save(user);
        return cardRepo.saveAll(output);
    }

    /**
     * Creates and adds several cards to a user's inventory. Then saves the new cards and the user.
     * 
     * @param username - the username of the user to add cards to
     * @param cardDataCollection - a collection of data used to create new UserCards
     * @return A collection of UserCards if successful. If not, null will be returned and nothing will be saved.
     * @throws UsernameNotFoundException if no user exists with that username
     * 
     * @see #addCards(User, Collection)
     */
    public Collection<UserCard> addCards(String username, Collection<CardDto> cardDataCollection) throws UsernameNotFoundException{
        Optional<User> result = userRepo.findByUsername(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username " + username);
        }
        return addCards(result.get(), cardDataCollection);
    }

    /**
     * Saves a UserCard in the database.
     * 
     * @param card - the card to save
     * @return a new instance of the saved card
     */
    public UserCard saveCard(UserCard card) {
        return cardRepo.save(card);
    }

    /**
     * Saves multiple UserCards in the database
     * 
     * @param cards - a collection of cards to save
     * @return a collection of the new instances of those saved cards
     */
    public Collection<UserCard> saveCards(Collection<UserCard> cards) {
        return cardRepo.saveAll(cards);
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