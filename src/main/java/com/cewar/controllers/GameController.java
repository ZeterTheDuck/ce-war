package com.cewar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public int newGame() {
        // STUB replace with correct data
        GameData newGame = new GameData(null, null, 0, 0, 0); // TODO get data from somewhere
        gameService.put(newGame);
        return newGame.getId(); // the ID of the created game
    }

    @MessageMapping("/message")   // If message is sent to /message, call this method
    @SendTo("/game/update") // Broadcast response to all subscribers of /game/update
    public GameResponseDto handleAction(GameMessageDto message) throws Exception {







        Thread.sleep(1000); // simulated delay
        return new GameResponseDto("Received message " + HtmlUtils.htmlEscape(message.getMessage())); // Sanitize to prevent attacks
    }
}
