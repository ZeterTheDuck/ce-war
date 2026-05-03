package com.cewar.cardGenerator;

import java.io.FileNotFoundException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.cewar.inventory.FilteredCardCollection;
import com.cewar.library.Card;
import com.cewar.library.CELib.*;

/**
 * Class to generate a pack
 * 
 * TODO Special art, such as legacy art, is not handled by this.
 */
public class PackGenerator {

    /**
     * Set of enums to represent different types of packs
     * @param type - corresponding archetype, for series packs
     */
    public enum Pack {
        ASSORTED(null), 
        STAPLE(null), 
        MATERIAL(null), 
        RARE(null),

        AQUATIC(Archetype.AQUATIC), 
        ARACHNIC(Archetype.ARACHNIC), 
        BUGGLE(Archetype.BUGGLE), 
        CODED(Archetype.CODED), 
        FEATHERY(Archetype.FEATHERY), 
        LIZAR(Archetype.LIZAR), 
        SKELL(Archetype.SKELL), 
        SLIME(Archetype.SLIME), 
        TAROT(Archetype.TAROT);

        private final Archetype seriesArchetype;
        private Pack(final Archetype type) {
            seriesArchetype = type;
        }
        /**
         * Converts this pack type to an archetype, if applicable.
         * If this pack type is not a series pack type, null is returned instead.
         */
        public Archetype toArchetype() {
            return this.seriesArchetype;
        }

    }

    /**
     * Possible rarities that generate() can choose
     */
    private enum Roll {
        COMMON,
        UNCOMMON,
        RARE,
        ULTRA_RARE,
        GOD_REPLICA
    }

    private static ArrayList<Card> cards;
    private static Random numGen;
    private final static int SERIES_STAPLE_CHANCE = 3; // Rarity of 1 in N of a series card just being a Staple card

    /**
     * Class to represent a card in a pack
     */
    static private class RollEntry {
        public Roll rarity;
        public boolean isShiny;
        public boolean isStaple; // Used for assorted packs

        /**
         * Constructor to represent a roll entry
         * 
         * @param rarity - card rarity, see Roll enum
         */
        private RollEntry(Roll rarity) {
            this.rarity = rarity;
            isShiny = false;
            isStaple = false;
        }

        /**
         * ONLY outputs the rarity of this card and if it is shiny.
         */
        public String toString() {
            if (isShiny) {
                return "SHINY_" + rarity.toString();
            }
            return rarity.toString();
        }

        /**
         * Generates a card, based on the given parameters.
         * 
         * @param type - type of pack to generate
         * @return Object array of length 4, containing data for a generated card, based on parameters. 
         *      <ul><li>Index 0: card id from database</li>
         *      <li>Index 1: if card is promo</li>
         *      <li>Index 2: if card is shiny</li>
         *      <li> Index 3: custom art source of card, if applicable. If there is none, it should be <code>null</code>.</li>
         * @throws UnexpectedException if Pack type is invalid
         */
        public CardDto generate(Pack type, Collection<Card> cardBank) throws UnexpectedException{
            FilteredCardCollection fcc = new FilteredCardCollection(cardBank);

            fcc.removeIf(Type.OTHER);

            // Apply filters based on pack type:
            switch (type) {
                case ASSORTED:
                    // Do nothing to filters, all cards should be available
                    break;
                case STAPLE:
                    // packFilter.addFilter(Archetype.STAPLE);
                    fcc.removeIfNot(Archetype.STAPLE);
                    break;
                case MATERIAL:
                    // packFilter.addFilter(Archetype.MATERIAL); // REVIEW may need to change to the card type instead.
                    fcc.removeIfNot(Archetype.MATERIAL);
                    break;
                case RARE:
                    // Do nothing to filters, all cards should be available
                    break;
                case AQUATIC: case ARACHNIC: case BUGGLE: case CODED: case FEATHERY: 
                case LIZAR: case SKELL: case SLIME: case TAROT:
                    if (isStaple) {
                        fcc.removeIfNot(Archetype.STAPLE);

                         // Exclude staples from major archetypes, including this one:
                        fcc.removeIf(Archetype.AQUATIC);
                        fcc.removeIf(Archetype.ARACHNIC);
                        fcc.removeIf(Archetype.BUGGLE);
                        fcc.removeIf(Archetype.CODED);
                        fcc.removeIf(Archetype.FEATHERY);
                        fcc.removeIf(Archetype.LIZAR);
                        fcc.removeIf(Archetype.SKELL);
                        fcc.removeIf(Archetype.SLIME);
                        fcc.removeIf(Archetype.TAROT);
                    } else {
                        fcc.removeIf(type.toArchetype());
                    }
                    break;
                default:
                    throw new UnexpectedException("Unexpected pack type requested");
            }

            // Apply filters based on card rarity:
            switch (rarity) {
                case COMMON:
                    fcc.removeIfNot(Rarity.COMMON);
                    break;
                case UNCOMMON:
                    fcc.removeIfNot(Rarity.UNCOMMON);
                    break;
                case RARE:
                    fcc.removeIfNot(Rarity.RARE);
                    break;
                case ULTRA_RARE:
                    fcc.removeIfNot(Rarity.ULTRA_RARE);
                    break;
                case GOD_REPLICA:
                    // Apply special filters now
                    ArrayList<Object> filters = new ArrayList<>();
                    filters.add(Type.GOD);
                    filters.add(Type.REPLICA);
                    fcc.removeIfNotAny(filters);
                    break;
            }

            if (fcc.size() != 0) {
                // Set output to a random Card that matches the set filters
                ArrayList<Card> allCards = new ArrayList<>();
                allCards.addAll(fcc);

                // Choose a random card from remaining items in FCC
                Card cardRef = allCards.get(numGen.nextInt(allCards.size()));

                // TODO replace "null" with a way to implement custom card art
                String artSource = null;

                return new CardDto(cardRef, artSource, false, isShiny);
            } else {
                // Special handling in the case that no cards are available
                // REVIEW At the moment, many archetypes are missing rarities.
                // Eventually this code should become redundant
                System.out.println("0 valid cards for given filters");
                return null;
            }
        }
    }

    /**
     * Generates a pack.
     * <p>
     * Spreadsheet for a visual of chances: {@link https://docs.google.com/spreadsheets/d/1eYTwYfTc5wiF-jkXkGl4bUpqLezYvu1l6jg2L5Wlpi0/edit}
     * 
     * @param type - type of pack to generate
     * @throws UnexpectedException - if pack type is invalid
=     */
    public static List<CardDto> generate(Pack type, Collection<Card> cardBank) throws UnexpectedException, FileNotFoundException {
        cards = new ArrayList<>();
        cards.addAll(cardBank);
        numGen = new Random();

        ArrayList<RollEntry> rolls = new ArrayList<RollEntry>();

        // SECTION Generate card rarities to request for the given pack type.
        switch (type) {
            // SECTION Assorted
            case ASSORTED:
                rolls.add(new RollEntry(Roll.COMMON));
                rolls.add(new RollEntry(Roll.COMMON));
                rolls.add(new RollEntry(Roll.COMMON));
                for (int i = 0; i < 2; i++) {
                    if (numGen.nextInt(2) == 0) {
                        rolls.add(new RollEntry(Roll.COMMON));
                    } else {
                        rolls.add(new RollEntry(Roll.UNCOMMON));
                    }
                }
                rolls.add(new RollEntry(Roll.UNCOMMON));
                rolls.add(new RollEntry(Roll.UNCOMMON));
                for (int i = 0; i < 2; i++) {
                    switch (numGen.nextInt(4)) {
                        case 0:
                            rolls.add(new RollEntry(Roll.COMMON));
                            break;
                        case 1:
                            rolls.add(new RollEntry(Roll.UNCOMMON));
                            break;
                        case 2: case 3:
                            rolls.add(new RollEntry(Roll.RARE));
                            break;
                    }
                }
                switch (numGen.nextInt(8)) {
                    case 0: case 1: case 2: case 3: case 4:
                        rolls.add(new RollEntry(Roll.RARE));
                        break;
                    case 5: case 6:
                        rolls.add(new RollEntry(Roll.ULTRA_RARE));
                        break;
                    case 7:
                        rolls.add(new RollEntry(Roll.GOD_REPLICA));
                        break;
                }
                findShiny(rolls);
                break;
                // !SECTION
            
            // SECTION Staple
            case STAPLE:
                for (int i = 0; i < 8; i++) {
                    rolls.add(new RollEntry(Roll.COMMON));
                }
                for (int i = 0; i < 4; i++) {
                    if (numGen.nextInt(2) == 0) {
                        rolls.add(new RollEntry(Roll.COMMON));
                    } else {
                        rolls.add(new RollEntry(Roll.UNCOMMON));
                    }
                }
                rolls.add(new RollEntry(Roll.UNCOMMON));
                rolls.add(new RollEntry(Roll.UNCOMMON));
                switch (numGen.nextInt(10)) {
                    case 0: case 1: case 2: case 3: case 4:
                        rolls.add(new RollEntry(Roll.UNCOMMON));
                        break;
                    case 5: case 6: case 7: case 8:
                        rolls.add(new RollEntry(Roll.RARE));
                        break;
                    case 9:
                        if (numGen.nextInt(2) == 0) {
                            rolls.add(new RollEntry(Roll.ULTRA_RARE));
                        } else {
                            rolls.add(new RollEntry(Roll.GOD_REPLICA));
                        }
                        break;
                }
                findShiny(rolls);
                break;
                // !SECTION

            // SECTION Material
            case MATERIAL:
                for (int i = 0; i < 8; i++) {
                    rolls.add(new RollEntry(Roll.COMMON));
                }
                if (numGen.nextInt(2) == 0) {
                    rolls.add(new RollEntry(Roll.COMMON));
                } else {
                    rolls.add(new RollEntry(Roll.UNCOMMON));
                }
                switch (numGen.nextInt(10)) {
                    case 0: case 1: case 2: case 3: case 4:
                        rolls.add(new RollEntry(Roll.UNCOMMON));
                        break;
                    case 5: case 6: case 7: case 8:
                        rolls.add(new RollEntry(Roll.RARE));
                        break;
                    case 9:
                        rolls.add(new RollEntry(Roll.ULTRA_RARE));
                        break;
                }
                findShiny(rolls);
                break;
                // !SECTION

            // SECTION Rare
            case RARE:
                for (int i = 0; i < 4; i++) {
                    if (numGen.nextInt(2) == 0) {
                        rolls.add(new RollEntry(Roll.COMMON));
                    } else {
                        rolls.add(new RollEntry(Roll.UNCOMMON));
                    }
                }
                for (int i = 0; i < 2; i++) {
                    if (numGen.nextInt(2) == 0) {
                        rolls.add(new RollEntry(Roll.UNCOMMON));
                    } else {
                        rolls.add(new RollEntry(Roll.RARE));
                    }
                }
                rolls.add(new RollEntry(Roll.RARE));
                rolls.add(new RollEntry(Roll.RARE));
                if (numGen.nextInt(2) == 0) {
                    rolls.add(new RollEntry(Roll.RARE));
                } else {
                    rolls.add(new RollEntry(Roll.ULTRA_RARE));
                }
                if (numGen.nextInt(2) == 0) {
                    rolls.add(new RollEntry(Roll.ULTRA_RARE));
                } else {
                    rolls.add(new RollEntry(Roll.GOD_REPLICA));
                }
                findShiny(rolls);
                findShiny(rolls);
                break;
                // !SECTION

            // SECTION Series
            case AQUATIC: case ARACHNIC: case BUGGLE: case CODED: case FEATHERY: 
            case LIZAR: case SKELL: case SLIME: case TAROT:
                // Series Pack
                rolls.add(new RollEntry(Roll.COMMON));
                rolls.add(new RollEntry(Roll.COMMON));
                rolls.add(new RollEntry(Roll.COMMON));
                for (int i = 0; i < 3; i++) {
                    if (numGen.nextInt(3) < 2) {
                        rolls.add(new RollEntry(Roll.COMMON));
                    } else {
                        rolls.add(new RollEntry(Roll.UNCOMMON));
                    }
                }
                rolls.add(new RollEntry(Roll.UNCOMMON));
                rolls.add(new RollEntry(Roll.UNCOMMON));
                if (numGen.nextInt(3) == 0) {
                    rolls.add(new RollEntry(Roll.UNCOMMON));
                } else {
                    rolls.add(new RollEntry(Roll.RARE));
                }
                if (numGen.nextInt(5) < 4) {
                    rolls.add(new RollEntry(Roll.RARE));
                } else {
                    if (numGen.nextInt(3) < 2) {
                        rolls.add(new RollEntry(Roll.ULTRA_RARE));
                    } else {
                        rolls.add(new RollEntry(Roll.GOD_REPLICA));
                    }
                }
                findShiny(rolls);
                for (RollEntry entry : rolls) {
                    if (numGen.nextInt(SERIES_STAPLE_CHANCE) == 0) {
                        entry.isStaple = true;
                    }
                }
                break;
                // !SECTION

            default :
                throw new UnexpectedException("Unexpected pack type requested");
        }
        // !SECTION Generate cards

        ArrayList<CardDto> generatedCards = new ArrayList<>();
        for (RollEntry roll : rolls) {
            generatedCards.add(roll.generate(type, cardBank));
        }

        return generatedCards;
    }
    
    /**
     * Helper method to determine if this pack should have a "shiny" variant.
     * Updates the card in the array itself
     * 
     * @param list - list of roll entries
     */
    private static void findShiny(ArrayList<RollEntry> list) {
        if (numGen.nextInt(4) == 0) {
            int index = 0;
            while (true) {
                index = numGen.nextInt(list.size());
                if (!list.get(index).isShiny) {
                    list.get(index).isShiny = true;
                    break;
                }
            }
        }
    }

}
