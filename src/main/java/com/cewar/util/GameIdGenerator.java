package com.cewar.util;

import java.time.Instant;

/**
 * Generates a random 5-upper-case letter String, based on the current date and time.
 * 
 * <p> This string is based on the current date and time, and is not guaranteed to be unique.
 * However, seeds will only repeat every 8.25 days, or if generated at the exact same time.
 */
public class GameIdGenerator {

    /**
     * The modulus to use to ensure IDs are 5 digits long. 
     * 
     * <p> Equal to 26^5.
     * */
    public static final long MODULUS = 11881376L;

    /**
     * Generates a random 5-letter string.
     * 
     * <p> This string is based on the current date and time, and is not guaranteed to be unique.
     * However, seeds will only repeat every 8.25 days, or if generated at the exact same time.
     * 
     * @return a 5-letter String of A-Z
     */
    public static String generateId() {
        // Use the number milliseconds since January 1, 1970 UTC as the seed
        return generateId(Instant.now().toEpochMilli());
    }

    /**
     * Converts a given seed to a game ID, which is a combination of 5 upper-case letters
     * 
     * @param seed - the value to use
     * @return a String of characters, A-Z
     */
    public static String generateId(long seed) {

        // Take modulus to get a number between 0 and MODULUS - 1
        // Then convert that remainder to alphabetical Base-26
        String seedBase26 = decimalToAlphabeticalBase26(seed % MODULUS);

        // Pad any leading zeros with 'A', to guarantee a 5-letter String
        return String.format("%5s", seedBase26) // Pad to length 5 with spaces
                .replace(' ', 'A'); // Replace spaces with 'A'
    }

    /**
     * Converts a non-negative Base-10 (0-9) value to alphabetical Base-26 (A-Z), where A=0
     * 
     * @param input - value to be converted
     * @return the input represented in alphabetical Base-26
     * 
     * @implNote negative values will cause unintended behavior, making the '-' symbol become '>'
     */
    private static String decimalToAlphabeticalBase26(long input) {

        // Use Java's built-in conversion that uses 0-9 and a-p
        char[] data = Long.toString(input, 26).toCharArray();

        /* Shift values to be A-Z; that is:
         * 0 -> A
         * 9 -> J
         * a -> K
         * p -> Z
         */
        for (int i = 0; i < data.length; i++) {
            if (data[i] <= '9') {
                data[i] = (char) (data[i] + 17); // Shift 0 -> A, 1 -> B, etc.
            } else {
                data[i] = (char) (data[i] - 22); // Shift a -> K, b -> L, etc.
            }
        }

        return String.valueOf(data);
    }
}
