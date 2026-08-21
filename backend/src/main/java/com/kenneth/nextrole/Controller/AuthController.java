package com.kenneth.nextrole.Controller;

import com.kenneth.nextrole.Service.AuthService;
import com.kenneth.nextrole.dto.auth.AuthResponse;
import com.kenneth.nextrole.dto.auth.LoginRequest;
import com.kenneth.nextrole.dto.auth.RegisterUserRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity <AuthResponse> register (@Valid @RequestBody RegisterUserRequest request){
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
