package com.sparta.team30.common.util;

import com.sparta.team30.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    public String makeToken(Date expiry, User user) {
        Date now = new Date();
        SecretKey key = getSecretKey();
        List<String> authorities = Collections.singletonList(user.getRole().getAuthority());
        return Jwts.builder()
                .header()
                .add("alg", "HS256")
                .and()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .subject(user.getUsername())
                .claim("id", user.getId())
                .claim("authorities", authorities)
                .signWith(key)
                .compact();
    }

    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser() // claim 조회
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }
}
