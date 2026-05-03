package com.cewar.library;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.cewar.library.CELib.*;

/**
 * Essentially a collection of Predicates, to filter through a database of cards
 * 
 * @deprecated Use FilteredCardCollection instead
 */
public class Filter implements Predicate<Card> {
    private boolean filterAll;
    ArrayList<Predicate<Card>> filters;

    public Filter(boolean filterAll) {
        this.filterAll = filterAll;
        filters = new ArrayList<Predicate<Card>>();
    }

    /**
     * Clears all the filters stored by this object
     */
    public void clear() {
        filters.clear(); 
    }

    /**
     * Negates all filters stored by this object
     */
    public void negateFilters() {
        for (int i = 0; i < filters.size(); i++) {
            filters.set(i, filters.get(i).negate());
        }
    }

    public void addFilter(Predicate<Card> predicate) {
        filters.add(predicate); 
    }

    public void addFilter(Rarity rarity) {
        filters.add(card -> card.getRarity().equals(rarity));
    }

    public void addFilter(Type type) {
        filters.add(card -> card.getType().equals(type));
    }

    public void addFilter(Attribute attribute) {
        filters.add(card -> {
            for (int i = 0; i < card.getAttributes().size(); i++) {
                if (card.getAttributes().get(i).equals(attribute)) {
                    return true;
                }
            }
            return false;
        } );
    }

    public void addFilter(Archetype archetype) {
        filters.add(card -> {
            for (int i = 0; i < card.getArchetypes().size(); i++) {
                if (card.getArchetypes().get(i).equals(archetype)) {
                    return true;
                }
            }
            return false;
        } );
    }

    public void addFilter(String string) {
        filters.add(card -> {
            if (card.cardDetails().contains(string)) {
                return true;
            }
            return false;
        });
    }

    /**
     * Tests a given card against all filters, using a logical OR,
     * meaning the given card needs to match at least one filter will pass
     * 
     * @param input - card to test
     * @return whether card matches any filter
     */
    private boolean testOR(Card input) {
        for (Predicate<Card> predicate : filters) {
            if (predicate.test(input)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests a given card against all filters, using a logical AND,
     * meaning the given card must match all filters to pass
     * 
     * @param input - card to test
     * @return whether card matches any filter
     */
    private boolean testAND(Card input) {
        for (Predicate<Card> predicate : filters) {
            if (!predicate.test(input)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Implementation of a predicate filter.
     * Uses a logical OR or AND, depending on the filterAll variable
     */
    @Override
    public boolean test(Card input) {
        if (filterAll) {
            return testAND(input);
        } else {
            return testOR(input);
        }
    }
    
}
