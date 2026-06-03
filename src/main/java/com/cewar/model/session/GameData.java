package com.cewar.model.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import com.cewar.enums.Location;

import lombok.Getter;
import lombok.Setter;

/**
 * Covers the per-game data
 */
public class GameData {

    @Getter
    private final String id;

    /**
     * The IDs of the players
     */
    @Getter @Setter
    private Long player1Id, player2Id;

    /**
     * A hashmap for all cards in play.
     */
    private HashMap<Long, GameCard> cards;

    /**
     * The dimensions of the playing field
     */
    @Getter
    private int width, height;

    /**
     * Constructor
     * 
     * @param id - a new game ID
     * @param width - the width of the field
     * @param height - the height of the field
     */
    public GameData(String id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    /**
     * Moves a card from its current location to another. 
     * 
     * <p> Sets the position to (0,0) when moved
     * 
     * <p> Use other {@link #moveCard(long cardId, Location destination, int x, int y) moveCard()} method for the field or draw slots.
     * 
     * @param cardId - ID of card to move
     * @param destination - {@link Location} enum of where the card should be moved to
     */
    public void moveCard(long cardId, Location destination) {

    }

    /**
     * Moves a card from its current location to another, with a given position.
     * 
     * <p> Use this method for moving cards to the field or draw slots.
     * 
     * @param cardId
     * @param destination
     * @param xPos
     * @param yPos
     */
    public void moveCard(long cardId, Location destination, int xPos, int yPos) {

    }

    /**
     * Attaches a card (or stack of cards) to another
     * 
     * <p> If the first card already has cards attached, those cards will stay attached to the first unless {@link #refreshStacking()} is called.
     * 
     * @param cardId - ID of the card to attach
     * @param cardIdTop - ID of the card to attach to
     * @throws NullPointerException if either card ID isn't in play
     */
    public void attachCard(long cardId, long cardIdTop) {
        cards.get(cardId).setAttachedTo(cards.get(cardIdTop));
    }

    /**
     * Stacks a card onto another card. The card being stacked (top) will take the same place as the one underneath.
     * 
     * @param cardIdTop
     * @param cardIdStackUnder
     */
    public void stackOn(long cardIdTop, long cardIdStackUnder) {

    }

    /**
     * Gets a collection of all cards in play.
     * 
     * @return a collection of {@link GameCard GameCards}
     */
    public Collection<GameCard> getAllCards() {
        return cards.values();
    }

    /**
     * Gets the card for a given ID, if it is in play
     * 
     * @param cardId
     * @return - the corresponding GameCard, or null if the card isn't in play
     */
    public GameCard getCard(long cardId) {
        return cards.get(cardId);
    }

    /**
     * Clears the counters and rotation status of a card, and sends any attached cards to the GY
     * 
     * @param cardId - ID of card to clear
     * @throws NullPointerException if the card ID isn't in play
     */
    public void clear(long cardId) {
        cards.get(cardId).clearCounters();
        cards.get(cardId).setRotated(false);

        // If a card is attached to the top card, move it to the GY
        cards.forEach((id, card) -> {
            GameCard topStack = card.getStackTop();
            if (card.equals(topStack)) {
                // Do nothing
            } else {
                this.clear(id);
                card.setLocation(Location.GRAVE);
            }
        });

    }

    /**
     * Ensures that all cards are either on the top of a stack, or directly linked
     * to the topmost card.
     * 
     * <p> Most likely unecessary
     */
    public void refreshStacking() {
        cards.forEach((id, card) -> {
            GameCard topStack = card.getStackTop();
            if (card.equals(topStack)) {
                card.setAttachedTo(null);
            } else {
                card.setAttachedTo(topStack);
            }
        });
    }

    /**
     * Sets the health of a card. Used for cards taking damage or healing.
     * 
     * @param cardId - ID of card
     * @param health - new health value
     */
    public void setHealth(long cardId, int health) {
        cards.get(cardId).setHealth(health);
    }

    /**
     * Temporarily sets the maximum health of a card. Does not restrict the bounds
     * of regular health. Is reset when that card is moved.
     * 
     * @param cardId    - ID of card
     * @param maxHealth - new maximum health value
     */
    public void setMaxHealth(long cardId, int maxHealth) {
        cards.get(cardId).setMaxHealth(maxHealth);
    }

    /**
     * Sets the attack value of a card.
     * 
     * @param cardId - ID of card
     * @param attack - new attack value
     */
    public void setAttack(long cardId, int attack) {
        cards.get(cardId).setAttack(attack);
    }

    /**
     * Picks a card from the corresponding player's deck, and adds it to their hand.
     * 
     * @param isPlayer1
     */
    public void drawCard(boolean isPlayer1) {
        drawRandomCard(isPlayer1).setLocation(Location.HAND);
    }

    /**
     * Adds the card to a player's hand from their draw slots.
     * 
     * <p> If there is no card, this does nothing.
     * 
     * <p> Does not select cards that are stacked onto other cards. This should never happen, but this is a safeguard.
     * 
     * @implNote Only modifies the first card, since there should only be one.
     * 
     * @param isPlayer1 - true if player 1 is drawing, false if it is player 2.
     * @param drawSlot - the index of the player's draw slots, starting at zero.
     */
    public void drawCard(boolean isPlayer1, int drawSlot) {
        int xPos;
        if (isPlayer1) {
            xPos = 1;
        } else {
            xPos = 2;
        }

        cards.forEach((id, card) -> {
            if ((card.getXPos() == xPos)
                && (card.getYPos() == drawSlot)
                && (card.getAttachedTo() == null)) {
                card.setLocation(Location.HAND);
                return;
            }
        });
    }

    /**
     * Picks a random card from the corresponding player's deck.
     * 
     * <p> Does not select cards that are stacked onto other cards. This should never happen, but this is a safeguard.
     * 
     * @return a random card from the player's deck, or null if there is none left.
     */
    private GameCard drawRandomCard(boolean isPlayer1) {
        long playerId;
        if (isPlayer1) {
            playerId = player1Id;
        } else {
            playerId = player2Id;
        }

        ArrayList<GameCard> deckContents = new ArrayList<>();
        cards.forEach((id, card) -> {
            if (card.getOwnerId().equals(playerId)
                    && card.getLocation().equals(Location.DECK)
                    && (card.getAttachedTo() == null)) {
                deckContents.add(card);
            }
        });

        if (deckContents.size() > 0) {
            return deckContents.get(new Random().nextInt(deckContents.size()));
        }
        return null;
    }

    /**
     * Replenishes all of the draw slots for a player
     */
    public void replenishDrawSlots(boolean isPlayer1) {
        int xPos;
        if (isPlayer1) {
            xPos = 1;
        } else {
            xPos = 2;
        }

        // If a card is in a draw slot, do not replenish that slot
        boolean[] emptyDrawSlots = {true, true, true};
        cards.forEach((id, card) -> {
            if ((card.getXPos() == xPos) && card.getLocation().equals(Location.DECK)) {
                // Can throw an exception if a card ends up in an invalid slot
                emptyDrawSlots[card.getYPos()] = false;
            }
        });

        // Replenish any draw slots that need to be replenished
        for (int i = 0; i < emptyDrawSlots.length; i++) {
            if (emptyDrawSlots[i]) {
                GameCard drawnCard = drawRandomCard(isPlayer1);
                if (drawnCard != null) {
                    drawnCard.setXPos(xPos);
                    drawnCard.setYPos(i);
                }
            }
        }
    }

    /**
     * Sets the rotation status of a card
     * 
     * @param cardId - ID of card
     * @param ownerIsOriginal - whether the would-be owner of this card is the actual owner of this card.
     * 
     * @throws NullPointerException if the card ID is invalid
     */
    public void rotate(long cardId, boolean ownerIsOriginal) {
        // Can throw an exception card ID is invalid
        cards.get(cardId).setRotated(!ownerIsOriginal);
    }

}
