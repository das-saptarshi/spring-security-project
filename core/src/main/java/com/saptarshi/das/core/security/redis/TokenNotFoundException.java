package com.saptarshi.das.core.security.redis;

public class TokenNotFoundException extends Exception {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
