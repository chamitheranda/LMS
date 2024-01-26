package com.chamith.lms.springlmsapi.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class GenerateJWT {

    private final String secretKey = "chamithEranda234chamithEranda234chamithEranda234";

    public String generateToken(String userEmail, String privilegeLevel) {
        long expirationTimeMillis = 86400000; // 1 day

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String token = Jwts.builder()
                .setSubject(userEmail)
                .claim("privilegeLevel", privilegeLevel)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public String extractSubject(String jwtToken) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
            String subject = claimsJws.getBody().getSubject();
            return subject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AuthenticationVerification validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                return new AuthenticationVerification(false);
            }
            String privilegeLevel = claims.get("privilegeLevel", String.class);
            return new AuthenticationVerification(true, privilegeLevel);

        } catch (ExpiredJwtException e) {
            return new AuthenticationVerification(false);
        }
    }
}