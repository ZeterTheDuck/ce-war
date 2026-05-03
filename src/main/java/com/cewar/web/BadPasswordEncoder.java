package com.cewar.web;

import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * Takes a password of any length and converts it into an "encrypted" string
 * This is mostly a theory test, that may or may not be used
 * 
 * Allowed characters:
 *      Alphabetical (a-z, A-Z)
 *      Numeric (0-9)
 *      !@#$%^&*() (Numbers but with the shift key held down)
 * 
 * @deprecated changed to use Bcrypt Encoding
 */
public class BadPasswordEncoder {
    // Starting String. Not really important.
    private final static String KEY = "fard123";
    // Set length of encoded Strings
    private final static int LENGTH = 20;
    // How many unique characters to represent Strings in. 
    private final static int RADIX = 16;

    /**
     * Encodes a given String
     * 
     * @param input
     * @return String of length 20, using 16 different characters (see instance variables)
     * @throws IllegalArgumentException if input contains illegal characters
     */
    public static String encode(String input) throws IllegalArgumentException {
        // Ensure input has valid characters. If not, throw exception
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9!@#$%^&*()]+");
        if (pattern.matcher(input).find()) {
            throw new IllegalArgumentException("Input string contains illegal characters!");
        }

        // Create starting value, based on key value
        BigInteger bigInt = new BigInteger(KEY, 36);

        // Encode input string into an integer value
        for (int i = 1; i < input.length() + 1; i++) {
            bigInt = bigInt.multiply(BigInteger.valueOf(((int) input.charAt(i - 1)) * i));
        }

        // Reverse the encoded value (idk if this will do much)
        bigInt = new BigInteger(new StringBuilder(bigInt.toString()).reverse().toString());

        // Define the maximum value for a base-RADIX for length LENGTH
        BigInteger modVal = new BigInteger(RADIX + "", 10);
        modVal = modVal.pow(LENGTH);

        while (bigInt.compareTo(modVal) > 0) {
            BigInteger[] bigDivRem = bigInt.divideAndRemainder(modVal);
            bigInt = bigDivRem[0].add(bigDivRem[1]);
        }

        // If outputted value would start with a 0, manually add the 0 back in
        StringBuilder output = new StringBuilder(bigInt.toString(RADIX));
        while (output.length() < 20) {
            output.insert(0, '0');
        }

        return output.toString();
    }
}
