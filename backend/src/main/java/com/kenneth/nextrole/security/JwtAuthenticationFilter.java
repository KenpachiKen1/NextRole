package com.kenneth.nextrole.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
Each file is essentially building off of each other.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomUserDetailsService customUserDetailsService){
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException {

        /*
        First initialize Header, Email, Jwt;
         */
       final String authHeader = request.getHeader("Authorization");
       final String email;
       final String jwt;

       //skipping the request if it doesn't start with Bearer
       if (authHeader == null || !authHeader.startsWith("Bearer ")){
           filterChain.doFilter(request, response);
           return;
       }
        //Getting the actual token
       jwt = authHeader.substring(7);

       //extracting the email from the token
       email = jwtService.extractEmail(jwt);

       //if email exists and the user is not already authenticated in this request
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                //validating the token here
                if(jwtService.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken upt =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    upt.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    //6. Put authenticated user into Spring Security context
                    SecurityContextHolder.getContext().setAuthentication(upt);
                }

        }

        // 7. Continue request
        filterChain.doFilter(request, response);

    }


}
