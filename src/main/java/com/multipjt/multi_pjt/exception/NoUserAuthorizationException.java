package com.multipjt.multi_pjt.exception;

public class NoUserAuthorizationException extends RuntimeException {
    public NoUserAuthorizationException(String message) {
        super(message);
    }
}
