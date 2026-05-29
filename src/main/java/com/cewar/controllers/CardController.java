package com.cewar.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cewar.model.entity.Card;
import com.cewar.services.CardService;
import com.cewar.util.CardReader;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller for handling requests for the card api/database.
 */
@RestController
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    private CardService cardService;

    /**
     * Gets all cards
     * 
     * @return all cards in database in JSON format
     */
    @GetMapping()
    public Iterable<Card> getAllCards() {
        return cardService.getAll();
    }

    /**
     * Gets a single card
     * 
     * @param cardId - ID of the card
     * @return card information in JSON format
     */
    @GetMapping("/{cardId}")
    public Card getCardById(@PathVariable String cardId) {
        return getCard(cardId);
    }

    /**
     * Helper method to get a card from the repository
     * 
     * @param cardId
     * @return
     */
    private Card getCard(String cardId) {
        try {
            return cardService.getById(cardId);
        } catch (EntityNotFoundException e) {
            return null; // REVIEW temporary fix, eventually the webpage should return a proper error
        }
    }

    /**
     * Deletes and repopulates card repository from the card index file.
     * 
     * <p> <strong>Authority:</strong> ADMIN
     * 
     * <p>
     * FIXME conflicts with MySQL foreign indexing and throws an error
     * 
     * @return
     * @throws IOException 
     * @throws JSONException
     */
    @GetMapping("/update")
    public Iterable<Card> updateRepository() throws IOException {
        // cardRepository.deleteAll();

        List<Card> allCards = CardReader.readCards();

        cardService.saveAll(allCards);

        return getAllCards();
    }

}
