package com.cewar.model.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cewar.enums.Authority;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.*;
import lombok.*;

/**
 * Class to represent a user. Stored in a database.
 * 
 * id - unique ID for a user (may not be needed)
 * username - user-entered username
 * password - encrypted password. Should not be stored as the original password.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    @Getter 
    private Long id;

    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String username;

    @Getter @Setter
    @Column(nullable = false)
    private String password;

    // The cards contained in this inventory
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="owner_id", referencedColumnName="user_id")
    @Getter
    private Collection<UserCard> inventory;

    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="owner_id", referencedColumnName="user_id")
    private Collection<UserDeck> decks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    @Getter @Setter
    private String email;

    @Getter @Setter
    @OneToOne(cascade = CascadeType.ALL)
    private UserInfo info;

    // Default constructor, required by Jackson
    protected User() {}

    /**
     * Constructs a user. Used by Jackson (JSON) to handle requests
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(
        @JsonProperty("username") String username, 
        @JsonProperty("password") String password, 
        @JsonProperty("authorities") JsonNode authority,
        @JsonProperty("userStats") UserInfo info,
        @JsonProperty("email") String email
        ) {
        if (username == null) {
            throw new IllegalArgumentException("Cannot create user: username must not be null.");
        }
        this.username = username;
        this.password = password;
        this.authority = Authority.authOf(authority.get(0).get("authority").asText());
        this.info = info;
        this.email = email;
        inventory = new ArrayList<>();
        decks = new ArrayList<>();
    }

    /**
     * Constructs a user from minimal data and automatically generates an ID.
     * 
     * Used for registering new users.
     * 
     * @param username
     * @param password
     * @param email
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        authority = Authority.USER;
        info = new UserInfo();
        info.setUsername(username);
        inventory = new ArrayList<>();
        decks = new ArrayList<>();

        // TODO add default inventory items, like a God card
    }

    /**
     * Gets all authorities given to this user.
     * Required by userDetails interface.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authority == null) {
            // This method must never return null, so return a blank authority.
            // REVIEW this may result in the user still being authenticated
            return Collections.singletonList(new SimpleGrantedAuthority(Authority.NONE.getAuthority()));
        }
        return Collections.singletonList(new SimpleGrantedAuthority(authority.getAuthority()));
    }

}
