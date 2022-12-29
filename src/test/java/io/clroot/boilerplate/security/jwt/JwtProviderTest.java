package io.clroot.boilerplate.security.jwt;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;

class JwtProviderTest {
    private static final String JWT_PREFIX_HS512 = "eyJhbGciOiJIUzUxMiJ9";
    private static final Duration ACCESS_TOKEN_EXPIRIRATION_DURATION = Duration.ofHours(2);
    private static final String ACCESS_TOKEN_SECRET = "some-random-secret-enough-to-HS512-for-accessToken";

    JwtProvider accessTokenProvider = new AccessTokenProvider(
        ACCESS_TOKEN_EXPIRIRATION_DURATION,
        ACCESS_TOKEN_SECRET,
        Duration.ofHours(1)
    );

    JwtProvider expiredAccessTokenProvider = new AccessTokenProvider(
        Duration.ofHours(-1),
        ACCESS_TOKEN_SECRET,
        Duration.ofHours(1)
    );

    @Test
    @DisplayName("issueToken() - HS512 알고리즘으로 토큰을 생성한다.")
    public void issueToken_IssueWithHS512() {
        //given
        String subject = "testSubject";

        //when
        var claims = JwtProvider.createClaims(subject);
        var token = accessTokenProvider.issueToken(claims);

        //then
        assertTrue(token.contains(JWT_PREFIX_HS512));
        assertTrue(accessTokenProvider.checkValidity(token));
    }

    @Test
    @DisplayName("getClaims() - 토큰을 파싱하여 Claims 를 가져온다.")
    public void getClaims_ParseAndGetClaimsFromToken() {
        //given
        String subject = "testSubject";
        Claims claims = JwtProvider.createClaims(subject);
        String token = accessTokenProvider.issueToken(claims);

        //when
        Claims parsedClaims = accessTokenProvider.getClaims(token);

        //then
        assertEquals(subject, parsedClaims.getSubject());
    }

    @Test
    @DisplayName("checkValidity() - 만료된 토큰은 검증에 실패한다.")
    public void checkValidity_False_TokenIsExpired() {
        //given
        String subject = "testWithExpiredTokenSubject";

        //when
        var claims = JwtProvider.createClaims(subject);
        var token = expiredAccessTokenProvider.issueToken(claims);

        //then
        assertFalse(expiredAccessTokenProvider.checkValidity(token));
    }
}
