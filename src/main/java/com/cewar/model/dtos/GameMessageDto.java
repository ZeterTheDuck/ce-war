package com.cewar.model.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

import com.cewar.model.session.AbstractGameAction;

/**
 * Client message DTO for games
 */
public class GameMessageDto {

    @Getter
    @Setter
    private List<AbstractGameAction> actions;

    public GameMessageDto() {

    }
}
