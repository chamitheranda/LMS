package com.chamith.lms.springlmsapi.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class GenerateJWT {
   public String generateToken(String userEmail){
       Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
       long expirationTimeMillis = 86400000; // 1 day

       String token = Jwts.builder()
               .setSubject(userEmail)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
               .signWith(SignatureAlgorithm.HS512, secretKey)
               .compact();

       return token;
   }
}
