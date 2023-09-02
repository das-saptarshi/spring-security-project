package com.saptarshi.das.admin.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends Exception {
    private final String responseMessage;

    public UserAlreadyExistsException(final String responseMessage) {
        this(null, responseMessage);
    }

    public UserAlreadyExistsException(final String message, final String responseMessage) {
        super(message);
        this.responseMessage = responseMessage;
    }
}
