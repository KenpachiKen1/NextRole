package com.kenneth.nextrole.dto.auth;


import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Getter @Setter
public class RegisterUserRequest {

    @Email @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

}
