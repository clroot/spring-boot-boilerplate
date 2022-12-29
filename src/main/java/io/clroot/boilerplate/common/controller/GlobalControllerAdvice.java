package io.clroot.boilerplate.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.clroot.boilerplate.common.dto.ErrorDto;
import io.clroot.boilerplate.common.exception.ApiException;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDto> handleException(final ApiException exception) {
        log.error("Error: ", exception);
        return handleExceptionInternal(exception, exception.getStatusCode());
    }

    @ExceptionHandler({EntityNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(final RuntimeException exception) {
        log.error("Error: ", exception);
        return handleExceptionInternal(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(final RuntimeException exception) {
        log.error("Error: ", exception);
        return handleExceptionInternal(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(final Exception exception) {
        log.error("Error: ", exception);
        return handleExceptionInternal(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDto> handleExceptionInternal(final Exception exception, HttpStatusCode status) {
        return ResponseEntity.status(status)
            .body(ErrorDto.builder()
                .statusCode(status.value())
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build());
    }

}
