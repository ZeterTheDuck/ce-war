package com.cewar.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cewar.model.entity.Card;
import com.cewar.repositories.CardRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service to access and manage Cards.
 */
@Service
public class CardService {
    @Autowired
    private CardRepository repo;

    public Card getById(String id) throws EntityNotFoundException{
        Optional<Card> result = repo.findCardById(id);
        if (result.isEmpty()) {
            throw new EntityNotFoundException("Card not found with ID of " + id);
        }
        return result.get();
    }

    public Collection<Card> getAll() {
        return repo.findAll();
    }

    public Card save(Card card) {
        return repo.save(card);
    }

    public Collection<Card> saveAll(Iterable<Card> cards) {
        return repo.saveAll(cards);
    }

    public void delete(Card card) {
        repo.delete(card);
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
