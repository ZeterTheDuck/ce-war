package com.cewar.model.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Client message DTO for games
 */
public class GameMessageDto {
    @Getter
    @Setter
    private String message;

    public GameMessageDto() {

    }

    public GameMessageDto(String message) {
        this.message = message;
    }
}
