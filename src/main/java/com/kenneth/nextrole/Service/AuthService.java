package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.UserRepository;
import com.kenneth.nextrole.dto.auth.AuthResponse;
import com.kenneth.nextrole.dto.auth.LoginRequest;
import com.kenneth.nextrole.dto.auth.RegisterUserRequest;
import com.kenneth.nextrole.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    /*
    This service does two jobs:
    1. Register
        - Check for duplicate emails, or usernames
        - Hash passwords
        - Create the user
        - Generate the token
        - Return auth response

     2. Login
        - Authenticate user/password with Spring security
        - Load user from DB
        - Generate token
        - Return auth response


     */

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService, PasswordEncoder
                               passwordEncoder,
                       AuthenticationManager authenticationManager

    ) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /*
    Register function
     */

    public AuthResponse register (RegisterUserRequest request){

        //Checking if the email already exists
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("This email is already in use");
        }
        //checking if the username already exists
        if(userRepository.findByUsernameIgnoreCase(request.getUsername()).isPresent()){
            throw new RuntimeException("This username is already in use");
        }
        User user = User.builder()
                .email(request.getEmail()).
                username(request.getUsername()).
                password(passwordEncoder.encode(request.getPassword())).build();

        user = userRepository.save(user);

        // Now we generate the token using spring security UserDetails shape
        return getAuthResponse(user);
    }

    public AuthResponse login (LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).
                orElseThrow(() -> new RuntimeException("User not found"));

        return getAuthResponse(user);
    }

    private AuthResponse getAuthResponse(User user) {
        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.Collections.emptyList()
                );

        String token = jwtService.generateToken(securityUser);

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }


}
