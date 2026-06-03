package com.cewar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.cewar.model.dtos.GameMessageDto;
import com.cewar.model.dtos.GameResponseDto;
import com.cewar.model.session.GameData;
import com.cewar.repositories.UserRepository;
import com.cewar.services.GameService;

/**
 * Controller for game API functions
 * 
 * TODO <p> Mappings should be modified to go to something like "/api/game"
 */
@Controller
@RequestMapping("/api/v1/games")
public class GameController {

    @Autowired
    private UserRepository userRepository;

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

    @MessageMapping("/message")   // If message is sent to /message, call this method
    @SendTo("/game/update") // Broadcast response to all subscribers of /game/update
    public GameResponseDto handleAction(GameMessageDto message) throws Exception {







        Thread.sleep(1000); // simulated delay
        // return new GameResponseDto("Received message " + HtmlUtils.htmlEscape(message.getMessage())); // Sanitize to prevent attacks
        return null; // STUB change this once GameMessageDto and GameResponseDto are updated
    }
}
