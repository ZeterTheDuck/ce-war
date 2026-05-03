package com.cewar.util;

/**
 * Contains a helper method to format a card name into a web-friendly String
 * @version June 10, 2025
 * @author Peter Madsen
 */
public class NameFormatter {

    /**
     * Formats a card name into a web-friendly String
     * 
     * - Replaces all spaces ( ) with underscores (_)
     * - Replaces all space-hyphen-space ( - ) with an underscore (_)
     * - Removes numerals from Tarot cards
     * - Removes all non-alphanumeric characters
     * 
     * @param cardName
     * @return
     */
    public static String formatName(String cardName) {
         // Create formatted name, for better user interface
        // File destination is stored as a string, relative to the "static" folder in the web application
        String fixedName = cardName;

        // Format cards to have proper names
        fixedName = fixedName
                        // For Tarot cards with numerals
                        .replaceAll("Tarot\\s-\\s[0A-Z]+\\s", "Tarot ")
                        // For cards with hyphens in name, including a couple Tarot cards that would not be formatted yet
                        .replaceAll("\\s-\\s", " ")
                        .replaceAll("-", " ")
                        // Remove any non-alphanumeric (or whitespace) characters
                        .replaceAll("[^a-zA-Z0-9\\s]*", "")
                        // Replace spaces with an underscore
                        .replaceAll("\\s", "_")
                        // Make lowercase
                        .toLowerCase();

        return fixedName;
    }
}
