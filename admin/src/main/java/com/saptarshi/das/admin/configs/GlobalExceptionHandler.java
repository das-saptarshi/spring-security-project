package com.saptarshi.das.admin.configs;

import com.saptarshi.das.admin.constants.ExceptionConstants;
import com.saptarshi.das.admin.exceptions.BaseException;
import com.saptarshi.das.admin.responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Component
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ BaseException.class })
    public ResponseEntity<ExceptionResponse> handleBaseException(
            final BaseException ex,
            final WebRequest request
    ) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(ex.getResponseMessage())
                .build();
        return new ResponseEntity<>(
                exceptionResponse,
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(
            final AuthenticationException ex,
            final WebRequest request
    ) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(ExceptionConstants.FORBIDDEN_USER_MESSAGE)
                .build();
        return new ResponseEntity<>(
                exceptionResponse,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(
            final AccessDeniedException ex,
            final WebRequest request
    ) {
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(ExceptionConstants.ACCESS_DENIED_MESSAGE)
                .build();
        return new ResponseEntity<>(
                exceptionResponse,
                HttpStatus.FORBIDDEN
        );
    }
}
