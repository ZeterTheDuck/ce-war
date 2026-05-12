package com.cewar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cewar.model.entity.User;
import com.cewar.services.UserService;
import com.cewar.repositories.UserRepository;

/**
 * Test class for {@link com.cewar.services.UserService Userservice}
 */
@SpringBootTest
public class TestUserService {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User("testUsername", "testPassword", "testEmail@example.com");
        
        Mockito.when(userRepository.findByUsernameIgnoreCase(testUser.getUsername())).thenReturn(testUser);
    }

    /**
     * Test that UserService calls UserRepository and returns the correct value for an existing user
     */
    @Test
    public void testGetUserByUsername() {
        assertTrue(userService.loadUserByUsername("testUsername").getUsername().equals(testUser.getUsername()));
    }

    /**
     * Test that UserService will throw a UsernameNotFoundException when a username without a matching user is used
     */
    @Test
    public void testGetNullUserByUsername() {
        try {
            userService.loadUserByUsername("fakeUsername");
        } catch (UsernameNotFoundException e) {
            if (e instanceof UsernameNotFoundException) {
                // Do nothing; test passes
                return;
            } else {
                fail(e.getClass() + " was thrown");
            }
        }
        fail("UsernameNotFoundException was not thrown");
    }

}
