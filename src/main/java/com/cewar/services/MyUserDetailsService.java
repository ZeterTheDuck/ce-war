package com.cewar.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cewar.model.entity.User;
import com.cewar.repositories.UserRepository;

/**
 * A User DAO (Data Access Object) helper class for DaoAuthenticationProvider.
 */
@Component
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found with username: " + username);
        }
        return user;
    }
    
}
