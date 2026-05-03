package com.cewar.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cewar.library.Card;

import lombok.Getter;

/**
 * Abstraction of a deck. 
 * 
 * <p> Used for the normal deck, the graveyard, dump, and hand.
 * 
 * @deprecated Cards now individually track where they are
 */
public class GameDeck {

    @Getter
    private ArrayList<Card> deck;

    /**
     * Creates a new empty deck
     */
    public GameDeck() {
        deck = new ArrayList<Card>();
    }

    /**
     * Creates a new deck from a list of cards
     * 
     * @param startingList - list of cards to put into deck
     */
    public GameDeck(List<Card> startingList) {
        deck = new ArrayList<Card>();
        for (Card card : startingList) {
            deck.add(card);
        }
    }

    /**
     * Adds a card to this deck
     * 
     * @param card - the card to add
     */
    public void add(Card card) {
        deck.add(card);
    }

    /**
     * Adds multiple cards to this deck
     * 
     * @param cards - a {@link List} of cards to add
     */
    public void addAll(List<Card> cards) {
        for (Card card : cards) {
            deck.add(card);
        }
    }

    /**
     * Gets a card at a given index
     */
    public Card get(int index) {
        return deck.get(index);
    }

    /**
     * Checks if this deck contains a certain card
     * 
     * <p> Probably not needed
     * 
     * @param card
     * @return
     */
    public boolean contains(Card card) {
        return deck.contains(card);
    }

    public boolean remove(Card card) {
        return deck.remove(card);
    }

    /**
     * Gets a random card from this deck.
     * 
     * @implnote Does not remove the card from this deck. Should be followed by calling {@link #remove()}
     * 
     * @return random card in this deck
     */
    public Card draw() {
        return deck.get(new Random().nextInt(deck.size()));
    }

}
