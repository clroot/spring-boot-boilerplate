package io.clroot.boilerplate.common.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

import io.clroot.boilerplate.common.constant.JwtConstant;

import jakarta.servlet.http.Cookie;

public class CookieUtils {
    public static String encode(String payload) {
        return URLEncoder.encode(payload, StandardCharsets.UTF_8);
    }

    public static String decode(String payload) {
        return URLDecoder.decode(payload, StandardCharsets.UTF_8);
    }

    public static Cookie createRefreshTokenCookie(String refreshToken) {
        String payload = StringUtils.isNotEmpty(refreshToken) ? encode(JwtConstant.HEADER_PREFIX + refreshToken) : null;
        Cookie cookie = new Cookie(JwtConstant.REFRESH_TOKEN_FIELD, payload);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 31);

        return cookie;
    }
}
