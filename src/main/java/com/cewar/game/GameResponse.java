package com.cewar.game;

import lombok.Getter;
import lombok.Setter;

/**
 * Server response DTO for games
 */
public class GameResponse {
    @Getter
    @Setter
    private String content;

    public GameResponse() {

    }

    public GameResponse(String content) {
        this.content = content;
    }
}
