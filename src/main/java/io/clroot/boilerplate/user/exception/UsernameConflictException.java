package io.clroot.boilerplate.user.exception;

import org.springframework.http.HttpStatus;

import io.clroot.boilerplate.common.exception.ApiException;

public class UsernameConflictException extends ApiException {
    public UsernameConflictException(final String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
