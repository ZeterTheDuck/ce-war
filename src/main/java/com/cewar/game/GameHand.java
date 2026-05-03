package com.cewar.game;

import java.util.List;

import com.cewar.library.Card;

/**
 * TODO WIP of a hand
 * 
 * <p> Hands should have an additional function for each card that toggles whether that 
 * card is visible to the opponent
 * 
 */
public class GameHand extends GameDeck {
    
    private class HandCard {
        public Card card;
        public boolean visible;

        private HandCard(Card card) {
            this(card, false);
        }

        private HandCard(Card card, boolean visible) {
            this.card = card;
            this.visible = visible;
        }
    }

    /**
     * Creates a new empty hand
     */
    public GameHand() {
        super();
    }

    /**
     * Creates a new hand from a list of cards
     * 
     * @param startingList - list of cards to put into deck
     */
    public GameHand(List<Card> startingList) {
        super(startingList);
    }
}
