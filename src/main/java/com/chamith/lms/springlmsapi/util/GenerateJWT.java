package com.chamith.lms.springlmsapi.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class GenerateJWT {
    Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateToken(String userEmail , String privilegeLevel){
       long expirationTimeMillis = 86400000; // 1 day

       String token = Jwts.builder()
               .setSubject(userEmail)
               .claim("privilegeLevel", privilegeLevel)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
               .signWith(SignatureAlgorithm.HS512, secretKey)
               .compact();

       return token;
   }

    public AuthenticationVerification validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                return new AuthenticationVerification(false);
            }
            String privilegeLevel = claims.get("privilegeLevel", String.class);
            return new AuthenticationVerification(
                    true ,
                    privilegeLevel
            ) ;

        } catch (ExpiredJwtException e) {
            return new AuthenticationVerification(false) ;
        }
    }
}
