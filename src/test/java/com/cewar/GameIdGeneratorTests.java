package com.cewar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import com.cewar.util.GameIdGenerator;

@SpringBootTest
public class GameIdGeneratorTests {

    /* testGenerateId() without a seed isn't planned to be tested, as it depends
        on the current time and testing it could be inconsistent. */
    
    /**
     * Test ID generation, ensuring no exceptions are thrown and that return value
     * matches expected format
     */
    @ParameterizedTest
    // Test all digits that would result in A-Z, as well as carryover and modulus limit
    @ValueSource(longs = {
            0L, 1L, 2L, 3L, 4L, 5L,
            6L, 7L, 8L, 9L, 10L, 11L,
            12L, 13L, 14L, 15L, 16L, 17L,
            18L, 19L, 20L, 21L, 22L, 23L,
            24L, 25L, 26L, 27L, 100L, 11881375L,
            11881376L})
    public void testGenerateId(long num) {
        // Assert that ID can generate and is a 5-uppercase-letter String
        assertTrue(GameIdGenerator.generateId(num).matches("[A-Z]{5}"),
                "ID generated from seed " + num + " did not match expected format.");
    }
}
