package com.kenneth.nextrole.security;


import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service

public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
            User user = userRepository.findByEmail(email).
                    orElseThrow(() -> new UsernameNotFoundException("No user found"));
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(), Collections.emptyList()
            );

    }

}
