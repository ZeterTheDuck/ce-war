package com.cewar.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.cewar.model.entity.Card;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for {@link #readCards()}
 */
public class CardReader {
    // File source of all cards, represented as JSON
    public static final String CARD_INDEX = "ce-war\\src\\main\\resources\\private\\cardIndex.json";

    /**
     * Takes the JSON file associated with the card data, and converts it into a list of {@link Card}s.
     * 
     * @return List of {@link Card}s
     * @throws IOException
     */
    public static List<Card> readCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Card> listCard = objectMapper.readValue(new File(CARD_INDEX), new TypeReference<List<Card>>(){});

        return listCard;
    }
}
