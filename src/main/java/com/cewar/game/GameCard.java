package com.cewar.game;

import java.util.HashMap;

import com.cewar.library.Card;
import com.cewar.enums.Location;
import com.cewar.web.userdetails.UserCard;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a dynamic card in play, having extra instance variables
 */
public class GameCard implements Comparable<GameCard> {

    /**
     * The user-card ID of the card that this card represents
     */
    @Getter
    private final Long ucid;

    /**
     * Original data of this card.
     */
    @Getter
    private final Card refCard;

    /**
     * This card's location on the field. If this card is attached to another card, this variable shouldn't matter.
     */
    @Getter @Setter
    private Location location;

    /**
     * The X and Y positions of this card. Ignored if {@link #cardLocation} is not "FIELD" or "DECK"
     * 
     * <p> If the X-position is negative:
     * <ul><li> -4 is P1's God card, Y-position does not matter.
     * <li> -3 is P2's God card, Y-position does not matter.
     * <li> -2 is P1's action slots. Y-position is the index.
     * <li> -1 is P2's action slots. Y-position is the index.</ul>
     * 
     * <p> If the {@link #cardLocation} is "DECK":
     * <ul><li> X-position of -2 is player 1's draw slots, Y-position indicates index
     * <li> X-position of -1 is player 2's draw slots, Y-position indicates index.
     * 
     * <p>
     * REVIEW Usage of X-position to indicate owner may not be necessary, there are not currently any cards
     * or combination of cards that could end up in the opponent's side field or deck
     */
    @Getter @Setter
    private int x, y;

    /**
     * AP and HP values for this card.
     */
    @Getter @Setter
    private int attack, health, maxHealth;

    /**
     * The card that this card is attached to
     * 
     * <p> If this value is null, then this card is the top card in a stack, or not on the field
     * 
     * @implNote Stacked cards and equipped cards are treated the same
     */
    @Getter @Setter
    private GameCard attachedTo;

    @Getter @Setter
    private HashMap<Long, GameCard> attachedCards;

    /**
     * Whether this card should display as upside-down. Used in-game to signify that the owner is different.
     * 
     * <p> A value of false indicates that this card is owned by its original owner, 
     * whereas a value of true indicates that this card is owned by the opposing player.
     */
    @Getter @Setter
    private boolean rotated;

    /**
     * Whether this card should be visible to the opponent. Used primarily for the player's hand and action slots
     * 
     * <p> This is not secure, as the opponent can check the JSON data of game updates.
     * 
     * <p> Cards in the (main) deck are always invisible, and cards in the GY and Dump are always visible.
     */
    @Getter @Setter
    private boolean visible;

    @Getter
    private HashMap<String, Integer> counters;

    /**
     * Primary constructor
     * 
     * @param userCard userCard to use for details
     */
    public GameCard(UserCard userCard) {
        refCard = userCard.asCard();
        ucid = userCard.getId();
        resetData(); // Set data to default
    }

    /**
     * Sets the corresponding number of counters attached
     * to this card
     * 
     * @param counter - name of counter
     * @param amount - amount to set
     * @return previous value, or null if there was none.
     */
    public int setCounter(String counter, int amount) {
        return counters.put(counter, amount);
    }

    /**
     * Resets all counters
     */
    public void clearCounters() {
        counters.clear();
    }

    /**
     * Recursively finds the topmost card that this card is stacked to.
     * 
     * @return the topmost card that this card is stacked to, or this card if it is the topmost in the stack
     */
    public GameCard getStackTop() {
        if (attachedTo == null) {
            return this;
        } else {
            return attachedTo.getStackTop();
        }
    }

    /**
     * Attach this card to another card, recursively repeating for cards attached to this card.
     * <p> Resets counter, health, attack, etc., and changes location and position to match other card.
     * 
     * @param other Card to attach this card to
     */
    public void attachTo(GameCard other) {
        // 1. Recursively repeat for all attached cards
        for (GameCard card : attachedCards.values()) {
            card.attachTo(other);
        }

        // 2. Clear data=
        resetData();

        // 3. Attach to other card
        attachedTo = other;
        this.location = other.getLocation();
        this.x = other.getX();
        this.y = other.getY();
    }

    /**
     * Moves a card to a specified location.
     * 
     * @param x
     * @param y
     * @param location
     */
    public void moveTo(int x, int y, Location location) {
        this.x = x;
        this.y = y;
        this.location = location;
    }

    @Override
    public int compareTo(GameCard o) {
        return ucid.compareTo(o.getUcid());
    }

    @Override
    public int hashCode() {
        return Long.hashCode(ucid);
    }

    /**
     * Resets this card's data to default. Use this when this card gets moved
     * 
     * <p> The following data is cleared:
     * <li> Health
     * <li> Attack
     * <li> Max Health
     * <li> Attached Cards (cards may still point to this card as the top)
     */
    private void resetData() {
        attack = refCard.getAttack();
        health = refCard.getHealth();
        maxHealth = refCard.getHealth();
        attachedCards = new HashMap<>();
        counters = new HashMap<>();
        attachedTo = null;
    }
    
}
