package com.kenneth.nextrole.Controller;

import com.kenneth.nextrole.Service.AuthService;
import com.kenneth.nextrole.dto.auth.AuthResponse;
import com.kenneth.nextrole.dto.auth.ForgotPasswordRequest;
import com.kenneth.nextrole.dto.auth.LoginRequest;
import com.kenneth.nextrole.dto.auth.RegisterUserRequest;
import com.kenneth.nextrole.dto.auth.ResetPasswordRequest;
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

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        authService.forgotPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body("If the email provided exists, a code will be sent to it's inbox. " +
                "Enter the code to reset your password");

    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        authService.resetPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body("Your password has been successfully reset");
    }

}
