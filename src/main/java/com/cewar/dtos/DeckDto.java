package com.cewar.dtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A Data Transfer Object to hold information of cards inside a deck.
 */
@Getter @Setter
public class DeckDto {

    /**
     * The ID of the deck
     */
    @NotNull
    private Long id;

    /**
     * The name of the deck
     * <p> If changed, name will be updated in database
     */
    @NotNull @NotEmpty
    private String name;

    /**
     * List of all {@link UserCard} IDs inside deck.
     */
    @NotNull
    private List<String> cardIds;

    public DeckDto() {

    }
}
