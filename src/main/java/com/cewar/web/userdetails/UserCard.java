package com.cewar.web.userdetails;

import java.util.Collection;

import com.cewar.cardGenerator.CardDto;
import com.cewar.cardGenerator.PackGenerator;
import com.cewar.library.Card;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Represents a card in a User's inventory
 * 
 * Is mapped by UserInventory once, and can exist in multiple UserDecks
 */
@Entity
public class UserCard {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Card Entity to reference
    @Getter
    @ManyToOne
    @JoinColumn(name = "card_ref")
    @NotNull(message = "Card reference must not be null")
    private Card cardRef;

    /**
     * ID of the owner of this card
     */
    @Column(name = "owner_id")
    @Getter
    private Long ownerId;

    /**
     * Any decks that contain this card
     */
    @ManyToMany(mappedBy = "contents")
    private Collection<UserDeck> deck;

    @Getter @Setter
    private boolean isPromo;

    @Getter @Setter
    private boolean isShiny;

    /**
     * Art source of this card, as in just the file name, like "feathery_duck.png"
     */
    @Getter @Setter
    private String artSource;

    /**
     * Default Constructor
     */
    public UserCard() {}

    /**
     * Constructor used by {@link PackGenerator} to construct a UserCard instance
     * 
     * @param owner - owner of card
     * @param card - array containing details of card. 
     * 
     * @see PackGenerator#generate(Pack type, Collection<Card> cardBank) (array at index 0 is type Card)
     */
    public UserCard(User owner, CardDto cardData) {
        this.ownerId = owner.getId();

        cardRef = cardData.getCardRef();
        isPromo = cardData.isPromo();
        isShiny = cardData.isShiny();
        artSource = cardData.getArtSource().replace("images/cards/art/", ""); // remove redundant parts of URL
    }

    /**
     * Helper method to get this card as a Card object that can be used in various methods.
     * 
     * @return this object as a Card
     */
    public Card asCard() {
        return new Card(cardRef, isPromo, isShiny, artSource, id);
    }

}
