package io.clroot.boilerplate.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class CookieUtilsTest {
    @Test
    @DisplayName("createRefreshTokenCookie() - token 이 null 이어도 예외를 던지지 않는다.")
    public void createRefreshTokenCookie_DoesNotThrow_TokenIsNull() {
        //given

        //when
        Executable executable = () -> CookieUtils.createRefreshTokenCookie(null);

        //then
        assertDoesNotThrow(executable);
    }

}
