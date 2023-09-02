package com.saptarshi.das.admin.exceptions;

import com.saptarshi.das.admin.constants.ExceptionConstants;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(final String responseMessage, final String logMessage) {
        super(responseMessage, logMessage);
    }

    public UserAlreadyExistsException(final String responseMessage) {
        super(responseMessage);
    }

    public UserAlreadyExistsException() {
        super();
    }
}
