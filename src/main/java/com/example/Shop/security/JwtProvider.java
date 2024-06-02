package com.example.Shop.security;


import com.example.Shop.exceptions.UserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private static final String AUTHORITIES = "authorities";

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String userName, Long ttl, Set<String> authorities) {
        Date expirationDateTime = Date.from(ZonedDateTime.now().plusMinutes(ttl).toInstant());
        return Jwts.builder()
                .setSubject(userName)
                .claim(AUTHORITIES, authorities)
                .signWith(getJwtKey(), SignatureAlgorithm.HS512)
                .setExpiration(expirationDateTime)
                .compact();
    }

    public boolean validate(String token) throws UserException.UserUnauthorized {
        if (token.isBlank()) {
            System.out.println("token is blank");
            throw new UserException.UserUnauthorized("Missing JWT");
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getJwtKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new UserException.UserUnauthorized("Invalid JWT");
        }
        return true;
    }

    public Authentication doAuthentication(String token) throws UserException.UserUnauthorized {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getJwtKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Set<SimpleGrantedAuthority> grantedAuthorities = ((ArrayList<String>) claims.get(AUTHORITIES)).stream()
                    .filter(claim -> claim != null && !claim.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            User user = new User(claims.getSubject(), "", grantedAuthorities);
            return new UsernamePasswordAuthenticationToken(user, "", grantedAuthorities);
        } catch (JwtException e) {
            throw new UserException.UserUnauthorized("Invalid JWT");
        }
    }

    private Key getJwtKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String logout() {
        return "";
    }

    public String getSubject(HttpServletRequest request) {
        String jwt = extractTokenFromRequest(request);

        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody();
        //String role = claims.get("role", String.class);
        return claims.getSubject();
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}

