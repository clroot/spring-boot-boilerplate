package io.clroot.boilerplate.security.jwt;

import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import io.clroot.boilerplate.common.constant.JwtConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

public abstract class JwtProvider {
    @Getter
    private final Duration expirationDuration;
    @Getter
    private final Duration reissueDuration;
    private final Key key;
    private final JwtParser parser;

    public JwtProvider(Duration expirationDuration, String secret, Duration reissueDuration) {
        this.expirationDuration = expirationDuration;
        this.key = createKey(secret);
        this.reissueDuration = reissueDuration;
        this.parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build();
    }

    public static Claims createClaims(String subject) {
        var claims = Jwts.claims();
        claims.setSubject(subject);
        return claims;
    }

    public Claims getClaims(String token) throws
        ExpiredJwtException,
        UnsupportedJwtException,
        MalformedJwtException,
        SignatureException,
        IllegalArgumentException {
        return parser.parseClaimsJws(token).getBody();
    }

    public String issueToken(Claims claims) {
        Date expiration = new Date(System.currentTimeMillis() + expirationDuration.toMillis());
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    public boolean checkValidity(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean shouldReissue(Claims claims) {
        Date reissueDate = new Date(System.currentTimeMillis() + reissueDuration.toMillis());
        return claims.getExpiration().before(reissueDate);
    }

    public boolean shouldReissue(String token) {
        try {
            Claims claims = getClaims(token);
            return shouldReissue(claims);
        } catch (Exception e) {
            return true;
        }
    }

    public String loadToken(HttpServletRequest request) {
        String payload = loadTokenInternal(request);
        if (StringUtils.isNotEmpty(payload) && payload.startsWith(JwtConstant.HEADER_PREFIX)) {
            return payload.replace(JwtConstant.HEADER_PREFIX, "");
        }
        return null;
    }

    protected abstract String loadTokenInternal(HttpServletRequest request);

    private Key createKey(String secret) {
        var secretBytes = Base64.getEncoder().encode(secret.getBytes());
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
