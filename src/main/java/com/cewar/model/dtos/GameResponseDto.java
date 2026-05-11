package com.cewar.model.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Server response DTO for games
 */
public class GameResponseDto {
    @Getter
    @Setter
    private String content;

    public GameResponseDto() {

    }

    public GameResponseDto(String content) {
        this.content = content;
    }
}
