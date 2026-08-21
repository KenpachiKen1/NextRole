package com.kenneth.nextrole.dto.auth;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class AuthResponse {

    private String token;
    private Long userId;
    private String username;
    private String email;
}
