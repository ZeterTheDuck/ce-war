package com.cewar.game;

import lombok.Getter;
import lombok.Setter;

/**
 * Client message DTO for games
 */
public class GameMessage {
    @Getter
    @Setter
    private String message;

    public GameMessage() {

    }

    public GameMessage(String message) {
        this.message = message;
    }
}
