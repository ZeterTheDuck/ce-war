package com.cewar.library;

/**
 * Library of enums
 */
public class CELib {
    /**
     * Rarity of a card
     */
    public enum Rarity { COMMON, UNCOMMON, RARE, ULTRA_RARE, NONE }

    /**
     * Type (color) of a card.
     */
    public enum Type { CREATURE, ACTION, BUILDING, MATERIAL, GOD, REPLICA, OTHER }

    /**
     * Attribute(s) of a card
     * 
     * <p> These are displayed before the name, except for GOD, which is displayed in place of the rarity symbol.
     */
    public enum Attribute {C1, C2, C3, C4, STAGE, EQUIP, GOD }

    /**
     * Archetype(s) of a card
     * 
     * <p> Consists of both series archetypes and functional archetypes
     */
    public enum Archetype {
        // Traditional Archetypes
        STAPLE, FEATHERY, SKELL, BUGGLE, AQUATIC, MATERIAL, TAROT, CODED, SLIME, ARACHNIC, LIZAR,
        HUMAN, FORTUNE, DRAGON,
                            
        // Functional Archetypes
        INFERIOR, SUPERIOR, ANIMATED, ANCHORED, CONJURED, DEPENDENT, IMPASSABLE, ACCESSIBLE, 
        DIRECTIONAL, AFLOAT, EXTRA
    }

    /**
     * Represents a counter on a card
     * 
     * @deprecated Counters are represented as Strings, not enums
     */
    public enum Counter {
        // Archetype Counters
        STAPLE, FEATHERY, SKELL, BUGGLE, AQUATIC, MATERIAL, TAROT, CODED, SLIME, ARACHNIC, LIZAR, HUMAN, FORTUNE, DRAGON,

        // Other Counters
        EFFECT, FUEL, JUSTICE, POISON, TIME
    }

    /**
     * Used by {@link com.cewar.game.GameCard GameCards} to refer to their location in a game.
     */
    public enum Location { HAND, DECK, FIELD, GRAVE, DUMP, EXTRA }

}
