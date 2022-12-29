package io.clroot.boilerplate.common.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

@Getter
public abstract class ApiException extends ResponseStatusException {

    protected final String message;

    public ApiException(final HttpStatusCode status, final String message) {
        super(status, message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
