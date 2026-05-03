package com.cewar.web.userdetails;

import jakarta.persistence.*;
import lombok.*;

/**
 * Holds public information of a user, as well as methods to access said info.
 */
@Entity
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private int wins;

    @Getter @Setter
    private int losses;

    @Getter @Setter
    private int draws;

    @Getter @Setter
    private int points;

    @Getter @Setter
    @Column(columnDefinition = "TEXT")
    // TODO make this better-suited for information
    private String bio;

    @Getter @Setter
    // TODO Implement a proper way for users to customize their profile picture via image upload
    private String profileURL;

    public UserInfo() {
        wins = 0;
        losses = 0;
        draws = 0;
        points = 10; // TODO adjust point values
        bio = "";
        username = "";
        profileURL = null;
    }

    UserInfo(int wins, int losses, int draws, int points) {
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.points = points;
    }
}
