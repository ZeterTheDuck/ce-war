package com.cewar.model.session;

import java.util.HashMap;
import java.util.List;

import com.cewar.enums.Location;

import lombok.Getter;
import lombok.Setter;

/**
 * Updates the data for a card during a game. Null values will not change existing data.
 */
@Getter
@Setter
public class SetCardGameAction extends AbstractGameAction {

    private Long
        ucid,
        attachedTo;

    private Location location;

    private Integer
        x,
        y,
        attack,
        health,
        max_health;
    
    private Boolean visible, rotated;

    private HashMap<String, Integer> counters;

    private List<Long> attachedCards;

    public SetCardGameAction() { }
}
