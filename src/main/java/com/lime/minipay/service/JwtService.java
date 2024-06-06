package com.lime.minipay.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiredMs}")
    private Long expiredJwtMs;

    public String getMemberLoginId(String token) {
        token = token.substring(7);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("loginId", String.class);
    }

    public boolean isExpired(String token, String secretKey) {
        token = token.substring(7); //Bearer 제외

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

    public boolean isExpired(String token) {
        return isExpired(token, secretKey);
    }

    public String createJwt(String memberLoginId) {
        Claims claims = Jwts.claims();
        claims.put("loginId", memberLoginId);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredJwtMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }
}
