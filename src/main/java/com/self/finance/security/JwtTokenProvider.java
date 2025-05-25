package com.self.finance.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret:your_jwt_secret}")
    private String JWT_SECRET;

    @Value("${jwt.expiration:3600000}")
    private long JWT_EXPIRATION;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        // You can enhance this to fetch user roles from DB or token claims if needed
        return new UsernamePasswordAuthenticationToken(
            username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}