package com.cewar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cewar.model.entity.UserDeck;

/**
 * A repository for {@link com.cewar.model.entity.UserDeck UserDecks}
 */
@Repository
public interface UserDeckRepository extends JpaRepository<UserDeck, Long> {

    /**
     * Finds a UserDeck by its Deck ID
     * 
     * @param id - the User-Deck ID of the deck
     * @return the UserDeck, if found 
     */
    Optional<UserDeck> findById(long id);

}