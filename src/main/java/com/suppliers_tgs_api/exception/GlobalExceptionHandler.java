package com.suppliers_tgs_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.suppliers_tgs_api.dto.response.ApisResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApisResponse<String> handleRuntimeException(
            RuntimeException ex
    ) {

        return new ApisResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }
}