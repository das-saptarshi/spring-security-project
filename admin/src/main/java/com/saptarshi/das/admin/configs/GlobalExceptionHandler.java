package com.saptarshi.das.admin.configs;

import com.saptarshi.das.admin.exceptions.BaseException;
import com.saptarshi.das.admin.responses.ExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;

import static com.saptarshi.das.admin.constants.ExceptionConstants.FORBIDDEN_USER_MESSAGE;
import static com.saptarshi.das.core.constants.ExceptionConstants.INVALID_TOKEN_MESSAGE;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBaseException(
            final BaseException ex,
            final WebRequest request
    ) {
        return ExceptionResponse.builder()
                .errors(Collections.singletonList(ex.getResponseMessage()))
                .build();
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAuthenticationException(
            final AuthenticationException ex,
            final WebRequest request
    ) {
        return ExceptionResponse.builder()
                .errors(Collections.singletonList(FORBIDDEN_USER_MESSAGE))
                .build();
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleAccessDeniedException(
            final AccessDeniedException ex,
            final WebRequest request
    ) {
        return ExceptionResponse.builder()
                .errors(Collections.singletonList(INVALID_TOKEN_MESSAGE))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleValidationException(
            final MethodArgumentNotValidException ex,
            final WebRequest request
    ) {
        final List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ExceptionResponse.builder()
                .errors(errors)
                .build();
    }
}
