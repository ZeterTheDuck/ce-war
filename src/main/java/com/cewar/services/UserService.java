package com.cewar.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cewar.model.entity.User;
import com.cewar.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service to access and manage Users.
 */
@Component
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /* Required for DaoAuthenticationProvider to work, would just be "getUserByUsername" if possible */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> result = userRepository.findByUsernameIgnoreCase(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Username not found with username: " + username);
        }
        return result.get();
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        return loadUserByUsername(username);
    }

    public User getByUsernameIgnoreCase(String username) throws UsernameNotFoundException {
        Optional<User> result = userRepository.findByUsernameIgnoreCase(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Username not found with username when ignoring case: " + username);
        }
        return result.get();
    }

    public User getByEmail(String email) {
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("User not found with email " + email);
        }
        return result.get();
    }

    /* SECTION CRUD Operations from CrudRepository */

    public User getById(long id) throws EntityNotFoundException {
        Optional<User> result = userRepository.findById(id);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("User not found with ID " + id);
        }
        return result.get();
    }

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Iterable<User> saveAll(Iterable<User> users) {
        return userRepository.saveAll(users);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    /* !SECTION */

}
