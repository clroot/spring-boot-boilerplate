package io.clroot.boilerplate.common.constant;

import org.springframework.http.HttpHeaders;

public class JwtConstant {
    public static final String HEADER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_FIELD = HttpHeaders.AUTHORIZATION;
    public static final String REFRESH_TOKEN_FIELD = "refreshToken";
}
