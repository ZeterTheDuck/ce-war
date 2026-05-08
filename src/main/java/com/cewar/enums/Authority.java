package com.cewar.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    /** Cannot do anything. For visitors that are not logged in. Only exists as to fulfill Spring's non-null contract. */
    NONE("ROLE_NONE"),      // NONE: 

    /** Can access login-blocked areas, like starting a game, and read other users' game stats */
    USER("ROLE_USER"),      // READ: Can access login-blocked areas, like starting a game, and read other users' game stats

    /** Can add infinite cards to their inventory without having to spend points. Useful for players transferring over. */
    WRITE("ROLE_WRITE"),    // WRITE: Can add infinite cards to their inventory without having to spend points. Useful for players transferring over

    /** Can manage other users and write to other users, as well as delete them */
    ADMIN("ROLE_ADMIN");

    private final String role;
    Authority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    /**
     * Converts a String representation of an authority into a value of this enum
     * 
     * <p> Use this method instead of Authority.valueOf() when the String would begin with "ROLE_" (added by Spring Security)
     * 
     * @param role - String representation of an authority beginning with "ROLE_"
     * @return Authority value representation of given role
     */
    public static Authority authOf(String role) {
        if (role == null) {
            throw new NullPointerException();
        }
        switch (role) {
            case "ROLE_NONE": return NONE;
            case "ROLE_USER": return USER;
            case "ROLE_WRITE": return WRITE;
            case "ROLE_ADMIN": return ADMIN;
            default:
                throw new IllegalArgumentException("Invalid authority of " + role);
        }
    }
}
