package com.raj.AnimalMovements.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.raj.AnimalMovements.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* filter intercepts incoming HTTP requests, extracts the JWT from the Authorization header, validates it, and 
    sets the authentication context. The filter runs only once per request.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtTokenUtil jwtTokenUtil; // Utility class for handling JWT operations.

    private final UserService userService; // Service to load user details.

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Retrieve the Authorization header from the request.
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the header is present and begins with "Bearer ".
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " to extract the token.
            username = jwtTokenUtil.getUsernameFromJwtToken(token); // Extract username from the token.
        }

        // If a username is found and no authentication exists in the SecurityContext...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Gets the username, password, and roles. Returns a Spring Security UserDetails object.
            UserDetails userDetails = userService.loadUserByUsername(username);

            // Validate the token.
            if (jwtTokenUtil.validateJwtToken(token)) {
                // Create an authentication object with the user details
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // Set the authentication in the security context so that it can be used by Spring Security for authorization for the rest of the request lifecycle. 
                //If token is not valid, then security context will not be set and the request will not be authenticated.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Continue processing the request.
        filterChain.doFilter(request, response);
    }
}