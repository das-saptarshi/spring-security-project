package com.saptarshi.das.admin.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.saptarshi.das.core.constants.SecurityConstants.AUTHORITIES_KEY;
import static com.saptarshi.das.core.constants.SecurityConstants.EXPIRATION_DURATION_IN_MILLISECONDS;
import static com.saptarshi.das.core.constants.SecurityConstants.PASSWORD_KEY;
import static com.saptarshi.das.core.constants.SecurityConstants.USERNAME_KEY;

@Service
public class JwtService {
    private static final String SECRET_KEY = "fa7eb9f8667c865173e585ef77c6263490eb59e8b679bf73bac84936a2e77cfd";

    public String extractUsername(final String token) {
        return extractClaims(token, claims -> claims.get(USERNAME_KEY, String.class));
    }

    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        final List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put(USERNAME_KEY, userDetails.getUsername());
        claims.put(PASSWORD_KEY, userDetails.getPassword());
        claims.put(AUTHORITIES_KEY, authorities);

        return generateToken(claims);
    }

    private String generateToken(final Map<String, Object> claims) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DURATION_IN_MILLISECONDS))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(final String token,
                                final UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(final String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private <T> T extractClaims(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
