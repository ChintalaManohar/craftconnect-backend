package com.craftconnect.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.craftconnect.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService= userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authHeader.substring(7);

        try {

        	String email =
        	        jwtService.extractUsername(token);

        	UserDetails userDetails =
        	        userDetailsService
        	                .loadUserByUsername(email);

        	if (jwtService.validateToken(
        	        token,
        	        userDetails.getUsername())) {

        	    UsernamePasswordAuthenticationToken authToken =
        	            new UsernamePasswordAuthenticationToken(
        	                    userDetails,
        	                    null,
        	                    userDetails.getAuthorities());
        	    
        	    System.out.println(
        	    	    "AUTHENTICATED USER: " + userDetails.getUsername()
        	    	);

        	    	System.out.println(
        	    	    "AUTHORITIES: " + userDetails.getAuthorities()
        	    	);

        	    SecurityContextHolder
        	            .getContext()
        	            .setAuthentication(authToken);
        	}

        } catch (Exception e) {

        	System.out.println(
        	        "JWT ERROR: " + e.getMessage()
        	);
        }

        filterChain.doFilter(request, response);
    }
}