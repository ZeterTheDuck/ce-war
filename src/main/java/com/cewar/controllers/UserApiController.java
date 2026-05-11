package com.cewar.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cewar.enums.Authority;
import com.cewar.model.dtos.CardDto;
import com.cewar.model.entity.User;
import com.cewar.model.entity.UserCard;
import com.cewar.repositories.CardRepository;
import com.cewar.repositories.UserRepository;

import java.util.NoSuchElementException;

// import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller that implements CRUD operations for USER
 * 
 * FIXME is not secured by users with ADMIN priviledges. May not be necessary
 * 
 */
@RestController
@RequestMapping("/api/users")
public class UserApiController {

    // ID to use if a user cannot be found
    public final static Long ERROR_ID = 0L;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Gets info from all users
     * @return Iterable of all users
     */
    @GetMapping
    public Iterable<User> getAllUsers() {
        // Return a static example database
        // return Arrays.asList(
        //     new User(1L, "Username", "Password"),
        //     new User(1L, "Joe", "Mama")
        //     );

        return userRepository.findAll();
    }

    /**
     * Gets info from a specific user at /api/users/{ID Number}
     * @return User object of requested user, or an error user with an ID of 0.
     */
    @GetMapping("/{id}")
    public User getUserByID(@PathVariable Long id) {
        return getUser(id);
    }

    /**
     * Creates a new user
     * 
     * @param user
     * @return
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PostMapping("/createAdmin")
    public User createAdmin(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Updates a user with new information
     * 
     * @param id - ID of user
     * @param user - Object of new user
     * @return
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User userData = getUser(id);
        if (userData.getId() == ERROR_ID) {
            return userData;
        }
        userData.setUsername(user.getUsername());
        userData.setPassword(user.getPassword());

        // Replace User in database with updated one
        return userRepository.save(userData);
    }

    /**
     * Deletes an existing user
     * 
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User userData = getUser(id);
        // if (userData.getId() == ERROR_ID) {
        //     return userData;
        // }
        userRepository.delete(userData);
        return ResponseEntity.ok().build();
    }

    /**
     * Adds a card to a user's inventory.
     * 
     * Secured to only users with the WRITE role (or ADMIN)
     * 
     * @param cardId - ID of card to add
     * @return
     */
    @GetMapping("/addCard/{cardId}")
    public ResponseEntity<?> addCard(@PathVariable String cardId) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.getAuthorities().contains(new SimpleGrantedAuthority(Authority.ADMIN.getAuthority())) 
            || user.getAuthorities().contains(new SimpleGrantedAuthority(Authority.WRITE.getAuthority()))) {
            try {
                // Add the card to the user's inventory
                user.getInventory().add(new UserCard(user, new CardDto(cardRepository.findById(cardId).get(), null, false, false)));
                userRepository.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Helper method to get a user by their ID
     * 
     * @param id
     * @return
     */
    private User getUser(Long id) {
        try {
            return userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            return null; // REVIEW temporary fix, eventually the webpage should return a proper error
        }
    }
    

}
