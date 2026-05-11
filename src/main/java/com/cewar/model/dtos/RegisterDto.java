package com.cewar.model.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A Data Transfer Object to hold information from the registration form
 */
@Getter @Setter
public class RegisterDto {
    /**
     * The username of this user.
     * 
     * <p>Allowed characters consist of:
     * <ul><li>All alphabet characters (A-Z, a-z)</li>
     * <li>All numbers (0-9)</li>
     * <li>Hyphen (-), Underscore (_), and Period (.)</li></ul>
     */
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String passwordConfirm;

    public RegisterDto() {

    }
}
