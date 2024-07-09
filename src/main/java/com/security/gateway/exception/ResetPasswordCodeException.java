package com.security.gateway.exception;

import lombok.Getter;

@Getter
public class ResetPasswordCodeException extends RuntimeException {
    private final Class tClass;
    public <T> ResetPasswordCodeException(String message, Class<T> tClass) {
        super(message);
        this.tClass = tClass;
    }

    public <T> ResetPasswordCodeException(String message, Throwable cause, Class<T> tClass) {
        super(message, cause);
        this.tClass = tClass;
    }
}