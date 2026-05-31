package com.cewar;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cewar.model.dtos.CardDto;
import com.cewar.model.entity.Card;
import com.cewar.model.entity.User;
import com.cewar.services.CardService;
import com.cewar.services.UserService;
import com.cewar.repositories.UserRepository;

/**
 * Test class for {@link com.cewar.services.UserService Userservice}
 */
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private CardService cardService;

    @MockitoBean
    private UserRepository userRepo;

    User testUser;
    Long testUserId;

    Card workingCard;

    @BeforeEach
    public void setUp() {
        testUser = new User("testUsername", "testPassword", "testEmail@example.com");
        workingCard = cardService.getById("feathery_duck");

        Mockito.when(userRepo.findByUsernameIgnoreCase(testUser.getUsername())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(userRepo.findByUsername(testUser.getUsername())).thenReturn(Optional.ofNullable(testUser));
        // NOTE this may need to get changed later if this behavior needs to be tested
        Mockito.when(userRepo.save(testUser)).thenReturn(testUser);
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

    /**
     * Test that UserService will successfully create and add a UserCard to a user's inventory
     */
    @Test
    public void testAddCard() {
        assertAll(
            () -> assertTrue(userService.addCard(testUser, new CardDto(workingCard, null, false, false)) != null),
            () -> assertTrue(userService.addCard(testUser.getUsername(), new CardDto(workingCard, null, false, false)) != null),
            () -> assertThrows(UsernameNotFoundException.class, 
                () -> userService.addCard("fakeUsername", new CardDto(workingCard, null, false, false)))
        );
    }

    /**
     * Test that UserService will successfully create and add multiple UserCards to a user's inventory
     */
    @Test
    public void testAddCards() {
        // Create list of CardDtos
        CardDto[] dtos = {
            new CardDto(workingCard, null, false, false),
            new CardDto(workingCard, null, false, false),
            new CardDto(workingCard, null, false, false)
        };

        assertTrue(userService.addManyCards(testUser, List.of(dtos)) != null);

        assertAll(
            () -> assertTrue(userService.addManyCards(testUser, List.of(dtos)) != null),
            () -> assertTrue(userService.addManyCards(testUser.getUsername(), List.of(dtos)) != null),
            () -> assertThrows(UsernameNotFoundException.class, 
                () -> userService.addManyCards("fakeUsername", List.of(dtos)))
        );
    }

    /**
     * Test that UserService will not throw errors when trying to get a user's inventory
     */
    @Test
    public void testGetUserInventory() {
        // Add a card to a test user's inventory for testing
        userService.addCard(testUser, new CardDto(workingCard, null, false, false));

        assertAll(
            () -> assertDoesNotThrow(() -> userService.getUserInventory(testUser)),
            () -> assertDoesNotThrow(() -> userService.getUserInventory(testUser.getUsername())),
            () -> assertDoesNotThrow(() -> userService.getUserInventoryAsCards(testUser)),
            () -> assertDoesNotThrow(() -> userService.getUserInventoryAsCards(testUser.getUsername()))
        );
    }
}