package com.cewar.model.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A request by a User to join a game.
 */
@Getter @Setter
public class GameConnectRequest {

    /**
     * The ID of the user connecting
     */
    @NotNull
    private long userId;
    
    /**
     * The ID of the deck the user is using
     */
    @NotNull
    private long deckId;

    /**
     * Default constructor
     */
    public GameConnectRequest() { }
}
