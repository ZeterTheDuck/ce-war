package com.cewar.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import com.cewar.library.Card;
import com.cewar.enums.*;

/**
 * A collection of cards that can easily be filtered.
 * 
 * <p> Used for generating packs via filtering repository and for searching through decks or user inventories.
 * 
 * <p> Cards cannot be added to this collection, only removed via filters or manually.
 */
public class FilteredCardCollection implements List<Card> {

    /**
     * Dictates which sorting method to use when this class is returned as an Array
     */
    public enum SortType {
        /**
         * Sorts by Card ID, same as alphabetical.
         * <p> Default option
         */
        IDENTITY((o1, o2) -> o1.getName().compareTo(o2.getName())),

        /**
         * Group by <strong>series</strong> archetype first, then sorts by ID. This is useful for properly
         * grouping cards that don't have the archetype listed in their name.
         * <p> Series Archetype is determined by the very first archetype listed. This is typically the archetype listed in the card's name.
         * <p> Archetypes are sorted alphabetically.
         */
        ARCHETYPE((o1, o2) -> {
            // Special handling in case card does not have any archetypes, such as Card Back or Gap
            if (o1.getArchetypes().size() == 0 ^ o2.getArchetypes().size() == 0) {
                if (o1.getArchetypes().size() == 0) {
                    // o1 has no archetypes, o2 does
                    // o2 comes before o1
                    return 1;
                } else {
                    // o2 has no archetypes, o1 does
                    // o1 comes before o2
                    return -1;
                }

            } else {
                if (o1.getArchetypes().size() == 0) {
                    // Neither o1 nor o2 have any archetypes
                    // Revert to sorting by ID
                    return o1.getName().compareTo(o2.getName());
                } else {
                    // Both o1 and o2 have archetypes
                    // Carry on with next methods
                }
            }

            if (o1.getArchetypes().get(0).equals(o2.getArchetypes().get(0))) {
                return o1.getName().compareTo(o2.getName());
            } else {
                return o1.getArchetypes().get(0).toString().compareTo(o2.getArchetypes().get(0).toString());
            }
        });
        
        private final Comparator<Card> comparator;

        SortType(Comparator<Card> cmp) {
            comparator = cmp;
        }

        public Comparator<Card> asComparator() {
            return comparator;
        }
    }

    private List<Card> allCards;

    /**
     * Constructor to create this object from a collection of cards.
     *  
     * <p> Default constuctor, sorts by ID
     * 
     * @param c - original colleciton of cards to filter
     */
    public FilteredCardCollection(Collection<Card> c) {
        this(c, SortType.IDENTITY);
    }

    /**
     * Constructor to create this object from a collection of cards.
     *  
     * @param c - original colleciton of cards to filter
     * @param sort - method to sort by
     */
    public FilteredCardCollection(Collection<Card> c, SortType sort) {
        allCards = new ArrayList<>(c);
        allCards.sort(sort.asComparator());
    }

    /**
     * Removes all cards from this collection that match the given filter.
     * 
     * <p> Single-filter version of {@link #removeIfAny}
     * 
     * @see removeIfAny
     * 
     * @param filter - desired card element
     */
    public boolean removeIf(Object filter) {
        // Convert
        return removeIfAny(Collections.singleton(filter));
    }

    /**
     * Removes all cards from this collection that match any given filter.
     * 
     * @see removeIfNotAny
     * 
     * @param filter - the filter to apply for each card. Allowed filter Object types: Rarity, Archetype, Attribute, Type, and String.<ul>
     *      <li>If the filter is not one of these types, it is ignored</li>
     *      <li>The String filter will search names, archetypes, card effect, and card flavor text, for any matching substrings. Case-insensitive and ignores any non-alphanumeric characters </li></ul>
     * @return if any elements were removed
     */
    public boolean removeIfAny(Collection<Object> filters) {
        Predicate<Card> predFilters = null;
        for (Object filter : filters) {
            if (predFilters == null) {
                predFilters = toPredicate(filter);
                continue;
            }
            predFilters = predFilters.or(toPredicate(filter));
        }

        return allCards.removeIf(predFilters);
    }

    /**
     * Removes all cards from this collection that match the given filter
     * 
     * <p> Single filter version of {@link #removeIfNotAny}
     * 
     * @see removeIfAny
     * 
     * @param filter - desired card element
     */
    public boolean removeIfNot(Object filter) {
        return removeIfNotAny(Collections.singleton(filter));
    }

    /**
     * Removes all cards from this collection that do NOT match any given filter.
     * 
     * @param filter - the filter to apply for each card. Allowed filter Object types: Rarity, Archetype, Attribute, Type, and String.<ul>
     *      <li>If the filter is not one of these types, it is ignored</li>
     *      <li>The String filter will search names, archetypes, card effect, and card flavor text, for any matching substrings. Case-insensitive and ignores any non-alphanumeric characters </li></ul>
     * @return if any elements were removed
     * 
     * @see removeIfAny
     */
    @SuppressWarnings("null")
    public boolean removeIfNotAny(Collection<Object> filters) {
        if (filters == null || filters.size() == 0) {
            return false;
        }
        Predicate<Card> predFilters = null;
        for (Object filter : filters) {
            if (predFilters == null) {
                predFilters = toPredicate(filter);
                continue;
            }
            predFilters = predFilters.or(toPredicate(filter));
        }

        return allCards.removeIf(predFilters.negate());
    }

    /**
     * Removes all cards from this collection that do NOT match ALL of the given filters.
     * 
     * @param filter - the filter to apply for each card. Allowed filter Object types: Rarity, Archetype, Attribute, Type, and String.<ul>
     *      <li>If the filter is not one of these types, it is ignored</li>
     *      <li>The String filter will search names, archetypes, card effect, and card flavor text, for any matching substrings. Case-insensitive and ignores any non-alphanumeric characters </li></ul>
     * @return if any elements were removed
     * 
     * @see removeIfAny
     */
    @SuppressWarnings("null")
    public boolean removeIfNotAll(Collection<Object> filters) {
        if (filters == null || filters.size() == 0) {
            return false;
        }
        Predicate<Card> predFilters = null;
        for (Object filter : filters) {
            if (predFilters == null) {
                predFilters = toPredicate(filter);
                continue;
            }
            predFilters = predFilters.and(toPredicate(filter));
        }

        return allCards.removeIf(predFilters.negate());
    }


    /**
     * Helper method to convert an Object to a predicate. Only works for valid types.
     * 
     * @param obj - object to convert
     * 
     * @see removeIfAny
     */
    private Predicate<Card> toPredicate(Object obj) {
        if (obj instanceof Rarity) {
            return (card-> card.getRarity().equals(obj));
        } else if (obj instanceof Archetype) {
            return (card -> card.getArchetypes().contains(obj));
        } else if (obj instanceof Attribute) {
            return (card -> card.getAttributes().contains(obj));
        } else if (obj instanceof Type) {
            return (card -> card.getType().equals(obj));
        } else if (obj instanceof String) {
            // Format filter to match how the card text will be formatted. See cardAsText()
            String filterString = ((String) obj).replaceAll("[\\W]", "").toLowerCase();

            return (card -> cardAsText(card).contains(filterString));
        } else {
            // Filter parameter is not a valid classtype, so simply return false
            return (card -> false);
        }
    }

    /**
     * Helper method to convert a card into searchable text, parsing out any non-alphanumeric characters.
     * 
     * This text will only consist of digits and lowercase letters. Separate fields will be separated by newline characters, 
     *      making it so you cannot search inbetween them, such as the last archetype and start of effect.
     * 
     * @param card card to represent
     * @return String representation of the card
     */
    private String cardAsText(Card card) {
        final String REGEX = "[\\W]";
        StringBuilder sb = new StringBuilder();
        sb.append(card.getName().replaceAll(REGEX, ""));
        sb.append("\n");
        sb.append(card.getArchetypes().toString().replaceAll(REGEX, ""));
        sb.append("\n");
        sb.append(card.getMaterials().replaceAll(REGEX, ""));
        sb.append("\n");
        sb.append(card.getEffect().replaceAll(REGEX, ""));
        sb.append("\n");
        sb.append(card.getFlavorText().replaceAll(REGEX, ""));

        return sb.toString().toLowerCase();
    }

    @Override
    public int size() {
        return allCards.size();
    }

    @Override
    public boolean isEmpty() {
        return allCards.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return allCards.contains(o);
    }

    @Override
    public Iterator<Card> iterator() {
        return allCards.iterator();
    }

    @Override
    public Object[] toArray() {
        return allCards.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return allCards.toArray(a);
    }

    @Override
    public boolean add(Card e) {
        throw new UnsupportedOperationException("'add' method is not supported");
    }

    @Override
    public boolean remove(Object o) {
        return allCards.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return allCards.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Card> c) {
        throw new UnsupportedOperationException("'addAll' method is not supported");
    }

    @Override
    public boolean addAll(int index, Collection<? extends Card> c) {
        throw new UnsupportedOperationException("'addAll' method is not supported");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return allCards.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return allCards.retainAll(c);
    }

    @Override
    public void clear() {
        allCards.clear();
    }

    @Override
    public Card get(int index) {
        return allCards.get(index);
    }

    @Override
    public Card set(int index, Card element) {
        throw new UnsupportedOperationException("'set' method is unsupported");
    }

    @Override
    public void add(int index, Card element) {
        throw new UnsupportedOperationException("'add' method is unsupported");
    }

    @Override
    public Card remove(int index) {
        return allCards.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return allCards.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return allCards.lastIndexOf(o);
    }

    @Override
    public ListIterator<Card> listIterator() {
        return allCards.listIterator();
    }

    @Override
    public ListIterator<Card> listIterator(int index) {
        return allCards.listIterator();
    }

    @Override
    public List<Card> subList(int fromIndex, int toIndex) {
        return allCards.subList(fromIndex, toIndex);
    }

}
