package com.cewar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cewar.repository.UserRepository;
import com.cewar.web.userdetails.User;

/**
 * Controller covering administrator methods in the administrator dashboard. 
 * All mappings under "/edit/**" are automatically secure
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String getEditPage() {
        return "admin";
    }

    /**
     * Creates a new user.
     */
    @PostMapping("/createUser")
    @ResponseBody
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * I forget what this does.
     * 
     * TODO make a way to add cards to a user's inventory
     * 
     * @param entityType
     * @return
     */
    @PostMapping("/add/user-card")
    @ResponseBody
    public String addCard(@ModelAttribute("entityType") String entityType) {
        
        return "your taking too long :)";
    }
}
