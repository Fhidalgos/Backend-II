package com.minimarket.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.issuer}")
    private String issuer;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Extraer el username del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer la fecha de expiración del token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraer el issuer del token
    public String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    // Extraer el identificador único del token
    public String extractJti(String token) {
        return extractClaim(token, Claims::getId);
    }

    // Extraer roles del token
    public Object extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles");
    }

    // Extraer un claim específico del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraer todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Verificar si el token está expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generar token con UserDetails, incluyendo roles, issuer y jti
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", roles);

        return createToken(claims, userDetails.getUsername());
    }

    // Método antiguo, por si alguna parte del código todavía lo usa
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // Crear token con claims, subject, issuer, jti, fecha de emisión y expiración
    private String createToken(Map<String, Object> claims, String subject) {
        Date fechaActual = new Date(System.currentTimeMillis());
        Date fechaExpiracion = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(issuer)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(fechaActual)
                .setExpiration(fechaExpiracion)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar token
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);

            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);

        } catch (Exception e) {
            return false;
        }
    }
}