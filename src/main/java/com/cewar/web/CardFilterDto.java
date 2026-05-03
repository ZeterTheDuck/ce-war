package com.cewar.web;

import com.cewar.library.CELib.*;

import lombok.Getter;
import lombok.Setter;

/**
 * A data transfer object for setting filters for {@link cardSearch.html}
 * 
 * @deprecated card filters are now handled client-side with {@link searchCardCollection.js}
 */
@Getter @Setter
public class CardFilterDto {

    private Rarity rarity;

    private Type type;

    private Attribute[] attributes;

    private Archetype[] archetypes;

    private String text;

    public CardFilterDto() {
        
    }
}