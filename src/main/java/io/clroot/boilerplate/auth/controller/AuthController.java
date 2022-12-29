package io.clroot.boilerplate.auth.controller;

import org.apache.commons.lang3.tuple.Triple;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.clroot.boilerplate.auth.dto.LoginRequestDto;
import io.clroot.boilerplate.auth.service.AuthService;
import io.clroot.boilerplate.common.constant.JwtConstant;
import io.clroot.boilerplate.common.utils.CookieUtils;
import io.clroot.boilerplate.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Authentication authentication = authService.getAuthentication(loginRequestDto);
        Triple<UserDto, String, String> result = authService.issueTokens(authentication);

        String accessToken = result.getMiddle();
        String refreshToken = result.getRight();

        setAccessTokenToHeader(response, accessToken);
        setRefreshTokenToCookie(response, refreshToken);

        return ResponseEntity.ok(result.getLeft());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request);

        setAccessTokenToHeader(response, null);
        setRefreshTokenToCookie(response, null);

        return ResponseEntity.ok().build();
    }

    void setAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(JwtConstant.ACCESS_TOKEN_FIELD, accessToken);
    }

    void setRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        response.addCookie(CookieUtils.createRefreshTokenCookie(refreshToken));
    }
}
