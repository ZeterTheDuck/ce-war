package com.cewar.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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