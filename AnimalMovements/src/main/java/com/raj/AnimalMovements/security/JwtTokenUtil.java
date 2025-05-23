package com.raj.AnimalMovements.security;

import java.util.Date;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.raj.AnimalMovements.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

/* Responsible for generating and validating JWT tokens */

@Component
public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    
    // Injects the secret key from application properties.
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Injects the token expiration time (in milliseconds) from application properties.
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // Generates a JWT token based on the authenticated user's details.
    public String generateJwtToken(Authentication authentication) {
        // Casts the authentication principal to Spring Security's User (which implements UserDetails).

        User userPrincipal = (User) authentication.getPrincipal();


        // Build and return a JWT token:
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // Sets the subject (username) in the token.
                .claim("userId", userPrincipal.getId()) // Sets the user ID claim in the token.
                .claim("role", userPrincipal.getRole().getRoleType()) // Sets the user role claim in the token.
                .claim("roleId", userPrincipal.getRole().getId()) // Sets the user role ID claim in the token.
                .setIssuedAt(new Date()) // Sets the token issue time.
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs)) // Sets token expiration time.
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512)
                .compact(); // Builds the token into a compact, URL-safe string.
    }

    // Extracts the username (subject) from the JWT token.
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Returns the subject (username) from the token.
    }

    public Long getUserIdFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class); // Returns the user ID from the token.
    }

    public String getRoleFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class); // Returns the user role from the token.
    }

    public Long getRoleIdFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roleId", Long.class); // Returns the user role ID from the token.
    }   

    // Validates the JWT token and returns true if it is valid.
    public boolean validateJwtToken(String authToken) {
        try {
            // Tries to parse the token. If parsing is successful, the token is valid.
            Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }
}

