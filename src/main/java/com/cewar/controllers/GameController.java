package com.cewar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.cewar.model.dtos.GameConnectRequest;
import com.cewar.model.dtos.GameMessageDto;
import com.cewar.model.dtos.GameResponseDto;
import com.cewar.model.entity.UserCard;
import com.cewar.model.session.GameCard;
import com.cewar.model.session.GameData;
import com.cewar.services.GameService;
import com.cewar.services.UserService;

import jakarta.persistence.EntityNotFoundException;

/**
 * Controller for game API functions
 * 
 * TODO <p> Mappings should be modified to go to something like "/api/game"
 */
@Controller
@RequestMapping("/api/v1/games")
public class GameController {

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @GetMapping("/create")
    public String newGame() {
        return gameService.createGame(3,5);
    }

    @GetMapping("/create")
    public String newGame(@RequestParam int width, @RequestParam int height) {
        return gameService.createGame(width, height);
    }

    /**
     * Connect a user to a game.
     * 
     * @param gameId - the ID of the game
     * @param request - a request containing the user's ID and deck ID
     * @return
     *  <code>400 Bad Request</code> if the deck is not owned by the user
     *  <code>404 Not Found</code> if no game exists with the specified ID
     */
    @PostMapping("/{gameId}/connect")
    public ResponseEntity<?> connectUser(@PathVariable String gameId, @RequestBody GameConnectRequest request) {
        // Get the GameData object associated with the game ID
        GameData game = gameService.get(gameId);
        if (game == null) {
            return new ResponseEntity<>("No game could be found with the ID " + gameId, HttpStatus.NOT_FOUND);
        }

        // Validate request
        try {
            // See if getting the User throws an error
            userService.getById(request.getUserId());
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        try {
            if (userService.getDeckById(request.getDeckId()).getOwnerId() != request.getUserId()) {
                return new ResponseEntity<>("Deck is not owned by user", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException e) {
            // Trying to get the Deck threw an error
            return new ResponseEntity<>("Deck not found", HttpStatus.NOT_FOUND);
        }

        // Add the user to the game 
        if (game.getPlayer1Id() == null) {
            game.setPlayer1Id(request.getUserId());
        } else if (game.getPlayer2Id() == null) {
            if (game.getPlayer1Id() == request.getUserId()) {
                return new ResponseEntity<>("User has already joined game", HttpStatus.FORBIDDEN);
            }
            game.setPlayer2Id(request.getUserId());
            
        } else {
            return new ResponseEntity<>("Game is full", HttpStatus.FORBIDDEN);
        }

        // Load cards into GameData
        for (UserCard card : userService.getDeckById(request.getDeckId()).getContents()) {
            game.loadCard(new GameCard(card));
        }

        // If at this point with no errors, return an OK response
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @MessageMapping("/message")   // If message is sent to /message, call this method
    @SendTo("/game/update") // Broadcast response to all subscribers of /game/update
    public GameResponseDto handleAction(GameMessageDto message) throws Exception {







        Thread.sleep(1000); // simulated delay
        // return new GameResponseDto("Received message " + HtmlUtils.htmlEscape(message.getMessage())); // Sanitize to prevent attacks
        return null; // STUB change this once GameMessageDto and GameResponseDto are updated
    }
}
