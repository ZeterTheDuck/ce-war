package com.cewar.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.cewar.game.GameMessage;
import com.cewar.game.GameResponse;

/**
 * Controller for game API functions
 * 
 * TODO <p> Mappings should be modified to go to something like "/api/game"
 */
@Controller
public class GameController {

    @MessageMapping("/message")   // If message is sent to /message, call this method
    @SendTo("/game/update") // Broadcast response to all subscribers of /game/update
    public GameResponse handleAction(GameMessage message) throws Exception {







        Thread.sleep(1000); // simulated delay
        return new GameResponse("Received message " + HtmlUtils.htmlEscape(message.getMessage())); // Sanitize to prevent attacks
    }
}
