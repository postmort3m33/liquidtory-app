package com.liquidtory.app.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // Method to generate token with role
    public String generateToken(UserDetails userDetails) {
        // Create a map of claims
        Map<String, Object> claims = new HashMap<>();

        // Get the role from userDetails, assuming there is only one role
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // Get role as string
                .findFirst()  // Assuming only one role
                .orElse("USER");  // Default to "ROLE_USER" if no role is found

        // Add the role to the claims map
        claims.put("role", role);

        // Create the JWT token using the claims map
        return Jwts.builder()
                .setClaims(claims)  // Add custom claims like role
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  // Set expiration time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Method to extract the username from the token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Method to validate the token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Method to check if the token is expired
    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Method to extract the role from the token (if needed)
    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);  // Extract "role" claim
    }
}