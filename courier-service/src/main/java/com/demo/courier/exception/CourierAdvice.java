package com.demo.courier.exception;


import com.demo.model.exception.ErrorResponse;
import com.demo.model.exception.ValidationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.demo.courier.constant.ErrorCodes.VALIDATION;

@RestControllerAdvice
@Slf4j
public class CourierAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exception) {

        commonLoggingError(exception);
        List<ValidationInfo> validations = exception.getBindingResult().getAllErrors().stream()
                .map(objectError -> ValidationInfo.builder()
                        .type(objectError.getCode())
                        .message(objectError.getDefaultMessage())
                        .build()).toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .code(VALIDATION)
                .validations(validations)
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CourierRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(CourierRuntimeException exception) {

        commonLoggingError(exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        commonLoggingError(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }

    private void commonLoggingError(Exception exception) {
        log.error("courier error {}", exception.getMessage());
    }
}