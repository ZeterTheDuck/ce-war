package com.cewar.game;

import java.util.HashMap;

import com.cewar.inventory.FilteredCardCollection;
import com.cewar.library.Card;
import com.cewar.web.userdetails.User;
import com.cewar.web.userdetails.UserDeck;

import lombok.Getter;

import com.cewar.library.CELib.*;

/**
 * Represents a player.
 * 
 * @deprecated use {@link GameData} instead
 * 
 * <p> Covers the following:
 * <ul><li> Deck
 * <li> Graveyard
 * <li> Dump
 * <li> Hand
 * <li> Draw Slots
 * <li> Original deck contents
 */
public class Player {

    @Getter
    private Long playerID;
    @Getter
    private GameDeck deck, grave, dump, hand;
    /**
     * All cards originally in the user's deck. Should not be modified.
     * 
     * @implNote Uses the user-card ID as the map key
     */
    @Getter
    private HashMap<Long, Card> allCards;
    @Getter
    private Card[] drawSlots;

    /**
     * Constructor for a player
     * 
     * @param player
     * @param startingDeck
     */
    public Player(User player, UserDeck startingDeck) throws IllegalArgumentException {
        allCards = new HashMap<>();
        for (Card card : startingDeck.getContentsAsCards()) {
            allCards.put(card.getUserCardId(), card);
        }

        this.playerID = player.getId();

        // Get all the cards that are meant to be in the deck, and add them to the deck.
        FilteredCardCollection fcc = new FilteredCardCollection(allCards.values());
        fcc.removeIf(Archetype.EXTRA);

        // NOTE - Old code that I'm keeping in case
        // Decks should only have 50-70 cards. Check may be ommitted in the future, make sure to first
        // parse out any God, replica, or "other" cards

        // if (parsedDeck.size() < 50 || parsedDeck.size() > 70) {
        //     throw new IllegalArgumentException("Invalid deck size of " + parsedDeck.size());
        // }

        deck = new GameDeck(fcc);
        drawSlots = new Card[3];
        grave = new GameDeck();
        dump = new GameDeck();
        hand = new GameDeck(); // REVIEW may want to add cards to hand here
        replenishDrawSlots();
    }

    /**
     * Gets the God card in this player's deck
     * 
     * <p> Assumes that there is one and only one God card in the deck
     * 
     * @return
     */
    public Card getGodCard() {
        FilteredCardCollection fcc = new FilteredCardCollection(allCards.values());
        fcc.removeIfNot(Type.GOD);
        return fcc.getFirst();
    }

    /**
     * Draw a random card from the deck, and move it to the hand
     */
    public void drawCard() {
        Card newCard = deck.draw();
        moveCard(newCard, Location.DECK, Location.HAND);
    }

    /**
     * Draw a card from the draw slots, and move it to the hand
     * 
     * @param index - draw slot index
     * 
     * @throws IllegalArgumentException If there is no card at the given index
     */
    public void drawCard(int index) {
        if (index < 0 || index > drawSlots.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (drawSlots[index] == null) {
            throw new IllegalArgumentException();
        }

        hand.add(drawSlots[index]);
        drawSlots[index] = null;
        //REVIEW - May delete reference
    }

    public void replenishDrawSlots() {
        for (int i = 0; i < drawSlots.length; i++) {
            if (drawSlots[i] == null) {
                drawSlots[i] = deck.draw();
                deck.remove(drawSlots[i]);
            }
        }
    }

    public void moveCard(Card card, Location source, Location destination) {
        switch (source) {
            case DECK:
                deck.remove(card);
                break;
            case GRAVE:
                grave.remove(card);
                break;
            case DUMP:
                dump.remove(card);
                break;
            case HAND:
                hand.remove(card);
            case FIELD:
                // TODO not implemented yet
        }

        switch (destination) {
            case DECK:
                deck.add(card);
                break;
            case GRAVE:
                grave.add(card);
                break;
            case DUMP:
                dump.add(card);
            case HAND:
                hand.add(card);
            case FIELD:
                // TODO not implemented yet
        }
    }
}
