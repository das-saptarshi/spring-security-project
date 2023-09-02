package com.saptarshi.das.admin.exceptions;

import com.saptarshi.das.admin.constants.ExceptionConstants;
import lombok.Getter;

@Getter
public abstract class BaseException extends Exception {
    private final String responseMessage;

    protected BaseException(final String responseMessage, final String logMessage) {
        super(logMessage);
        this.responseMessage = responseMessage;
    }

    protected BaseException(final String responseMessage) {
        this(responseMessage, ExceptionConstants.SOME_WENT_WRONG_MESSAGE);
    }

    protected BaseException() {
        this(ExceptionConstants.SOME_WENT_WRONG_MESSAGE);
    }
    
    public String getLogMessage() {
        return super.getMessage();
    }
}
