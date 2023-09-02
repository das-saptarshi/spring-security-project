package com.saptarshi.das.admin.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends BaseException {
    public UserNotFoundException(final String responseMessage, final String logMessage) {
        super(responseMessage, logMessage);
    }

    public UserNotFoundException(final String responseMessage) {
        super(responseMessage);
    }

    public UserNotFoundException() {
        super();
    }
}
