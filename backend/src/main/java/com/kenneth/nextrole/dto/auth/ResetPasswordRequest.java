package com.kenneth.nextrole.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class ResetPasswordRequest {

    @Email @NotBlank
    private String email;

    @NotBlank
    private String code;

    @NotBlank @Length(min = 3)
    private String newPassword;
}
