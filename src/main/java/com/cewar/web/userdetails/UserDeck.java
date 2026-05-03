package com.cewar.web.userdetails;

import java.util.ArrayList;
import java.util.Collection;

import com.cewar.library.Card;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a single deck of cards, used by a User.
 */
@Entity
public class UserDeck {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of this deck
     */
    @Getter @Setter
    private String name;

    /**
     * The ID of the user that owns this deck
     */
    @Getter
    @Column(name = "owner_id")
    private Long ownerId;

    /**
     * A table of cards mapped to this deck
     */
    @Getter
    @Setter
    @ManyToMany
    @JoinTable(
        name = "deck_card",
        joinColumns = @JoinColumn(name = "deck_id"), // ID of this deck
        inverseJoinColumns = @JoinColumn(name = "card_id")  // ID of a card in this deck. Not to be confused with the card's namespace ID, such as "feathery_duck".
                                                            // This is an long representing the card in the server's database
    )
    private Collection<UserCard> contents;

    /**
     * Default Constructor
     */
    public UserDeck() {}

    /**
     * Constructor for a deck with a name
     */
    public UserDeck(String deckName, User owner) {
        name = deckName;
        this.ownerId = owner.getId();
    }

    public Collection<Card> getContentsAsCards() {
        Collection<Card> output = new ArrayList<>();
        for (UserCard card : contents) {
            output.add(card.asCard());
        }
        return output;
    }
}
