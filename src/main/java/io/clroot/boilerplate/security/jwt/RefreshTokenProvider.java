package io.clroot.boilerplate.security.jwt;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.clroot.boilerplate.common.constant.JwtConstant;
import io.clroot.boilerplate.common.utils.CookieUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component("refreshTokenProvider")
public class RefreshTokenProvider extends JwtProvider {
    public RefreshTokenProvider(
        @Value("${app.auth.refreshTokenExpirationDuration}") Duration duration,
        @Value("${app.auth.refreshTokenSecret}") String secret,
        @Value("${app.auth.refreshTokenReissueDuration}") Duration reissueDuration) {
        super(duration, secret, reissueDuration);
    }

    @Override
    protected String loadTokenInternal(HttpServletRequest request) {
        var refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
            .filter(cookie -> StringUtils.equals(cookie.getName(), JwtConstant.REFRESH_TOKEN_FIELD))
            .findFirst();

        if (refreshToken.isPresent()) {
            String payload = refreshToken.get().getValue();
            return CookieUtils.decode(payload);
        }

        return null;
    }
}
