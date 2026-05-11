package com.cewar.model.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * A data transfer object for handling user login information
 */
@Getter @Setter
public class LoginDto {
    @NotNull
    @NotEmpty
    private String username;
    
    @NotNull
    @NotEmpty
    private String password;

    public LoginDto() {

    }

    public LoginDto (String username, String password) {
        this.username = username;
        this.password = password;
    }
}
