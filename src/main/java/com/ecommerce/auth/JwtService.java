package com.ecommerce.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Serviço de geração e validação de JWT.
 */
@Service
public class JwtService {

    // Chave HMAC-SHA256 (mínimo 32 bytes)
    private static final String SECRET_KEY =
        "bXlfc3VwZXJfc2VjcmV0X2tlX3dpdGhfMzJfYnl0ZXNfbWluaW11bV9zdXBlcl9zZWNyZXRfa2V5X2Zvcl9oYXNoXzI1Ng==";

    // Token válido por 24 horas
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    /**
     * Chave de assinatura HMAC-SHA256.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Gera um token JWT com claims customizados.
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Extrai o username (email) do token.
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrai a role do token.
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /**
     * Extrai todas as claims do token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * Verifica se o token é válido para o usuário informado.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
            && !isTokenExpired(token);
    }

    /**
     * Verifica se o token expirou.
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
            .getExpiration()
            .before(new Date());
    }
}
