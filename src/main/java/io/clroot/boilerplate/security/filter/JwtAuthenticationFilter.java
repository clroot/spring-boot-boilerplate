package io.clroot.boilerplate.security.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.clroot.boilerplate.auth.service.AuthService;
import io.clroot.boilerplate.common.constant.JwtConstant;
import io.clroot.boilerplate.common.utils.CookieUtils;
import io.clroot.boilerplate.security.jwt.JwtProvider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String accessToken = accessTokenProvider.loadToken(request);
        String refreshToken = refreshTokenProvider.loadToken(request);

        if (StringUtils.isNotEmpty(accessToken) || StringUtils.isNotEmpty(refreshToken)) {
            if (accessTokenProvider.shouldReissue(accessToken) && refreshTokenProvider.checkValidity(refreshToken)) {
                if (authService.checkExistRefreshTokenOnDB(refreshToken)) {
                    Claims claims = refreshTokenProvider.getClaims(refreshToken);
                    String newAccessToken = accessTokenProvider.issueToken(claims);
                    response.addHeader(JwtConstant.ACCESS_TOKEN_FIELD, JwtConstant.HEADER_PREFIX + newAccessToken);
                    accessToken = newAccessToken;

                    if (refreshTokenProvider.shouldReissue(refreshToken)) {
                        String newRefreshToken = authService.reissueRefreshToken(refreshToken);
                        response.addCookie(CookieUtils.createRefreshTokenCookie(newRefreshToken));
                    }
                } else {
                    response.addCookie(CookieUtils.createRefreshTokenCookie(null));
                }
            }
            if (accessTokenProvider.checkValidity(accessToken)) {
                Claims claims = accessTokenProvider.getClaims(accessToken);
                Authentication authentication = authService.getAuthentication(claims.getSubject());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
