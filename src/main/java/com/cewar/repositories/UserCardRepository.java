package com.cewar.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cewar.model.entity.UserCard;

/**
 * A repository for {@link com.cewar.model.entity.UserCard UserCards}
 */
@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    /**
     * Finds a UserCard by its User-Card ID
     * 
     * @param id - the User-Card ID of the card
     * @return the UserCard, if found 
     */
    Optional<UserCard> findById(long id);

}
