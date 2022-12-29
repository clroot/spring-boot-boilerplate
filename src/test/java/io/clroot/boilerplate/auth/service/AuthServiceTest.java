package io.clroot.boilerplate.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.mock.mockito.SpyBean;

import io.clroot.boilerplate.AbstractBackendTest;
import io.clroot.boilerplate.auth.model.RefreshToken;

class AuthServiceTest extends AbstractBackendTest {
    @SpyBean
    private Clock clock;

    @SpyBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        given(clock.instant()).willReturn(Instant.parse("2023-01-01T00:00:00Z"));
        given(authService.getRefreshReissueWatingDuration()).willReturn(Duration.ofDays(10));
    }

    @Test
    @DisplayName("checkRefreshTokenReissueAllowed() - 재발급 가능 기간의 리프레쉬 토큰 재발급 요청은 성공한다.")
    public void checkRefreshTokenReissueAllowed_Success_AppropriateReissueRequest() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
            .lastUsedAt(LocalDateTime.now().minusDays(11))
            .build();

        //when
        Executable checkRefreshTokenReissueAllowed = () -> authService.checkRefreshTokenReissueAllowed(refreshToken);

        //then
        assertDoesNotThrow(checkRefreshTokenReissueAllowed);
    }

    @Test
    @DisplayName("checkRefreshTokenReissueAllowed() - 잦은 리프레쉬 토큰 재발급 요청은 실패한다.")
    public void checkRefreshTokenReissueAllowed_ExceptionThrown_TooFastReissueRequest() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
            .lastUsedAt(LocalDateTime.now().minusDays(7))
            .build();

        //when
        Executable checkRefreshTokenReissueAllowed = () -> authService.checkRefreshTokenReissueAllowed(refreshToken);

        //then
        assertThrows(IllegalStateException.class, checkRefreshTokenReissueAllowed);
    }

}
