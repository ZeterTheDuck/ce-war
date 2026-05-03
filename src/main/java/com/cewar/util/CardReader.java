package com.cewar.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.cewar.library.Card;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
public class CardReader {
    // File source of all cards, represented as JSON
    public static final String CARD_INDEX = "ce-war\\src\\main\\resources\\private\\cardIndex.json";

    public static List<Card> readCards() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Card> listCard = objectMapper.readValue(new File(CARD_INDEX), new TypeReference<List<Card>>(){});

        return listCard;
    }
}
