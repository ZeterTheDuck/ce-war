package com.cewar.model.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO to create a new game.
 * 
 * @see com.cewar.controllers.GameController#newGame() GameController#newGame()
 */
@Getter
@Setter
public class GameCreateRequest {

    /**
     * The ID of the users who are playing.
     */
    @NotNull @Positive
    private Long user1Id, user2Id;

    /**
     * The deck IDs corresponding to the desired deck each user is using.
     */
    @NotNull @Positive
    private Long user1DeckId, user2DeckId;

    // Default Constructor
    public GameCreateRequest() { }
}
