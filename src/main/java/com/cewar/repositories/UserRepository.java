package com.cewar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cewar.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User corresponding to a given username
     * 
     * @implNote This is only used for registering Users to prevent confusion and impersonation from similar names. Use {@link #findByUsername(String)} instead.
     * 
     * @param username
     * @return
     */
    User findByUsernameIgnoreCase(String username);

    /**
     * Finds a User corresponding to a given username
     * 
     * @see #findByUsernameIgnoreCase(String)
     * 
     * @param username
     * @return
     */
    User findByUsername(String username);

    User findById(long id);

    User findByEmail(String email);
}