package com.saptarshi.das.admin.configs;

import com.saptarshi.das.admin.exceptions.BaseException;
import com.saptarshi.das.admin.responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ExceptionResponse> handleOtherException(
            final Exception ex,
            final WebRequest request
            ) {
        final String message = "Oops... Something went wrong! Please try after sometime.";
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(message)
                .build();
        return new ResponseEntity<>(
                exceptionResponse,
                HttpStatus.BAD_REQUEST
        );
    }

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
}
