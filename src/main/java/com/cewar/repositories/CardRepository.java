package com.cewar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cewar.model.entity.Card;

/**
 * Repository to get card information from a database
 */
@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    Card findCardById(String id);
}
