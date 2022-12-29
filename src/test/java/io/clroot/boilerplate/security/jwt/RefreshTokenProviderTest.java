package io.clroot.boilerplate.security.jwt;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.mock.web.MockHttpServletRequest;

class RefreshTokenProviderTest {
    private static final Duration REFRESH_TOKEN_EXPIRATION_TIME = Duration.ofDays(14);
    private static final String REFRESH_TOKEN_SECRET = "some-long-secret-text-for-refresh-token";
    private static final Duration REFRESH_TOKEN_REISSUE_TIME = Duration.ofDays(1);
    RefreshTokenProvider refreshTokenProvider = new RefreshTokenProvider(
        REFRESH_TOKEN_EXPIRATION_TIME,
        REFRESH_TOKEN_SECRET,
        REFRESH_TOKEN_REISSUE_TIME
    );

    @Test
    @DisplayName("loadToken() 쿠키가 null 이더라도 예외를 던지지 않는다.")
    public void loadToken_DoesNotThrow_CookieIsNull() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        Executable executable = () -> refreshTokenProvider.loadToken(request);

        //then
        assertDoesNotThrow(executable);
    }

}
