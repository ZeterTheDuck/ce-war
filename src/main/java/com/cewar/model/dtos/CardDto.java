package com.cewar.model.dtos;

import com.cewar.model.entity.Card;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

/**
 * A modifiable in-memory version of a {@link com.cewar.model.entity.Card Card} that can easily be modified without
 * dealing with database conflicts for creating {@link com.cewar.model.entity.UserCard UserCards}
 * 
 * @param cardId
 * @param artSource
 * @param isPromo
 * @param isShiny
 */
@Getter @Setter
public class CardDto {
    private Card cardRef;
    /**
     * Source URL of the art to use for this card, relative to this server in <code>images/cards/art/</code>
     */
    private String artSource;
    private boolean isPromo;
    private boolean isShiny;

    public CardDto() {

    }

    /**
     * Constructs a card from given information and a Card object.
     * 
     * @param cardRef - Card to reference. Should not modified.
     * @param artSrc - Source URL of the art to use for this card, relative to this server. If this value is null, the art source is pulled from the card reference
     * @param isPromo
     * @param isShiny
     */
    public CardDto(Card cardRef, @Nullable String artSource, boolean isPromo, boolean isShiny) {
        this.cardRef = cardRef;
        if (artSource == null) {
            this.artSource = cardRef.getArtSource();
        } else {
            this.artSource = artSource;
        }
    }
}
