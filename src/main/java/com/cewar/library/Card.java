package com.cewar.library;

import java.util.ArrayList;
import java.util.List;

import com.cewar.library.CELib.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;

/**
 * Represents a static card in a database
 */
@Entity
@Getter
public class Card {

    @Id
    private String id; // Similar to name, but formatted for web accessibility
    private String name;
    @Enumerated(EnumType.STRING)
    private Rarity rarity;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING) @ElementCollection
    private List<Attribute> attributes;
    @Enumerated(EnumType.STRING) @ElementCollection
    private List<Archetype> archetypes;
    /**
     * Used to identify cards owned by users. This is the ID of the card in the database.
     * 
     * <p> The default value for cards (not owned by users) is -1
     */
    private Long userCardId;

    /**
     * Attack value of this card
     * <p> Special values are used to display as certain values:
     * <ul><li>-1: does not display a number. May be replaced with something else like size.</li>
     * <li>-2: displays as "???", value is usually defined in card description. Can still 
     *      be set by the user during a game.</li></ul>
     */
    private int attack;

    /**
     * Size of this card, if applicable
     * <p> A null array means that the size is not displayed. Internally, this should default to 1x1.
     * <p> Can either be null or length 2
     */
    @ElementCollection private List<Integer> size;

    /**
     * Health of this card
     * <p> Special values are used to display as certain values:
     * <ul><li>-1: does not display a number.</li>
     * <li>-2: displays as "???", value is usually defined in card description. Can still 
     *      be set by the user during a game.</li></ul>
     */
    private int health;

    /**
     * Whether this card is a God card or not. Can be set independently from Type or Attributes, but those should usually be the same.
     * <p> If true, the health value is displayed with a <[]> symbol (number of card slots)
     */
    private boolean isGod;

    private String materials; // TODO storing as a String for now. Maybe later store as an Array?
    @Column(columnDefinition = "TEXT")
    private String effect;
    @Column(columnDefinition = "TEXT")
    private String flavorText;

    /**
     * Used by GameCard and UserCard
     * <p> For access from the database, this value will always be false.
     */
    private boolean isPromo, isShiny;

    /**
     * File path for just the art on the card 
     */
    private String artSource;

    /**
     * File path for the entire pre-rendered card (downloaded from the Google Drive)
     */
    private String fullResSource;
    
    /**
     * Extra constructor for creating a card from an existing card.
     * Allows for setting custom properties that cannot be changed later
     * Ideally used for viewing a player's inventory
     * 
     * <p> Used by {@link UserCard} to represent itself as a Card.
     * 
     * @param cardName
     * @param isPromo
     * @param isShiny
     * @param artSource - file name of source of art. For example, "feathery_duck_legacy.png"
     */
    public Card(Card refCard, boolean isPromo, boolean isShiny, String artSource, Long userCardId) {

        this.id = refCard.id;
        this.name = refCard.name;
        this.rarity = refCard.rarity;
        this.type = refCard.type;
        this.attributes = refCard.attributes;
        this.archetypes = refCard.archetypes;
        this.attack = refCard.attack;
        this.size = refCard.size;
        this.health = refCard.health;
        this.isGod = refCard.isGod;
        this.materials = refCard.materials;
        this.effect = refCard.effect;
        this.flavorText = refCard.flavorText;

        this.isPromo = isPromo;
        this.isShiny = isShiny;

        this.artSource = "/images/cards/art/" + artSource;
        this.fullResSource = "/images/cards/full/" + artSource;

        this.userCardId = userCardId;
    }

    /**
     * Helper constructor to create a blank card. Used for the getCopy() method.
     */
    private Card() {}

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    private Card(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("rarity") Rarity rarity,
        @JsonProperty("type") Type type,
        @JsonProperty("attribute") List<Attribute> attributes,
        @JsonProperty("archetype") List<Archetype> archetypes,
        @JsonProperty("attack") int attack,
        @JsonProperty("size") List<Integer> size,
        @JsonProperty("is_god") boolean isGod,
        @JsonProperty("health") int health,
        @JsonProperty("material") String materials,
        @JsonProperty("effect") String effect,
        @JsonProperty("flavor_text") String flavorText,
        @JsonProperty("art") String artSource,
        @JsonProperty("full") String fullResSource,
        @JsonProperty("user_card_id") Long userCardId
    ) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.type = type;
        this.attributes = attributes;
        this.archetypes = archetypes;
        this.attack = attack;
        this.size = size;
        this.health = health;
        this.isGod = isGod;
        this.materials = materials;
        this.effect = effect;
        this.flavorText = flavorText;

        this.isPromo = false;
        this.isShiny = false;

        this.artSource = artSource;
        this.fullResSource = fullResSource;

        this.userCardId = userCardId;
    }

    /**
     * Generates a deep copy of this Card, and allows for cosmetic changes to this card
     * 
     * @return a deep copy of this card
     */
    @JsonIgnore // Jackson for some reason tries to scan this method, sending it into a recursive loop.
    public Card getCopy() {
        Card cloneCard = new Card();

        cloneCard.id = id;
        cloneCard.name = name;
        cloneCard.rarity = rarity;
        cloneCard.type = type;
        cloneCard.attributes = new ArrayList<>(attributes);
        cloneCard.archetypes = new ArrayList<>(archetypes);
        cloneCard.attack = attack;
        cloneCard.size = new ArrayList<>(size); 
        cloneCard.health = health;
        cloneCard.isGod = isGod;
        cloneCard.materials = materials;
        cloneCard.effect = effect;
        cloneCard.flavorText = flavorText;
        cloneCard.isPromo = isPromo;
        cloneCard.isShiny = isShiny;
        cloneCard.artSource = artSource; // REVIEW - may need to say "= new File(artSource.getAbsolutePath());" and check for null values
        cloneCard.fullResSource = fullResSource;
        cloneCard.userCardId = userCardId;

        return cloneCard;
    }

    /**
     * Generates a simple string representation of this card.
     */
    public String toString() {
        return name + ", " + attack + "ATK, " + health + "HP";
    }

    /**
     * Generates a detailed string representation of this card.
     */
    public String cardDetails() {
        StringBuilder output = new StringBuilder();
        output.append("Name: " + name);
        output.append("\n- Rarity: " + rarity);
        output.append("\n- Type: " + type);
        output.append("\n- Attributes: " + attributes.toString());
        output.append("\n- Archetypes: " + archetypes.toString());
        switch (attack) {
            case (-1):
                // Append nothing
                break;
            case (-2):
                output.append("\n- Attack: ???");
                break;
            default:
                output.append("\n- Attack: " + attack);
        }
        if (size.size() == 0) {
            // Append nothing
        } else {
            output.append("\n- Size: " + size.get(0) + "x" + size.get(1));
        }
        switch (health) {
            case (-1):
                // Append nothing
                break;
            case (-2):
                output.append("\n- Health: ???");
                break;
            default:
                output.append("\n- Health: " + health);
        }
        if (isGod) {
            output.append(" <[]>");
        }
        if (materials.length() == 0) {
            // Append nothing
        } else {
            output.append("\n- Materials: " + materials);
        }
        output.append("\n- Effect: " + effect);
        output.append("\n- Flavor Text: " + flavorText);
        if (isPromo) {
            output.append("\n- Promo");
        }
        if (isShiny) {
            output.append("\n- Shiny");
        }
        output.append(artSource);
        output.append("\n");

        return output.toString();
    }

}
