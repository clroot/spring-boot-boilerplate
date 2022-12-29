package io.clroot.boilerplate.security.jwt;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.clroot.boilerplate.common.constant.JwtConstant;

import jakarta.servlet.http.HttpServletRequest;

@Component("accessTokenProvider")
public class AccessTokenProvider extends JwtProvider {
    public AccessTokenProvider(
        @Value("${app.auth.accessTokenExpirationDuration}") Duration duration,
        @Value("${app.auth.accessTokenSecret}") String secret,
        @Value("${app.auth.accessTokenReissueDuration}") Duration reissueDuration) {
        super(duration, secret, reissueDuration);
    }

    @Override
    protected String loadTokenInternal(HttpServletRequest request) {
        return request.getHeader(JwtConstant.ACCESS_TOKEN_FIELD);
    }
}
