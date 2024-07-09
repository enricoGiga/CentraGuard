package com.security.gateway.exception;

public class VerifyTokenException extends RuntimeException {
    public VerifyTokenException(String message) {
        super(message);
    }

    public VerifyTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
