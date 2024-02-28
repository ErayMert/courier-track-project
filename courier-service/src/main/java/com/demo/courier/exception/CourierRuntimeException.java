package com.demo.courier.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter

public class CourierRuntimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private int code;
    private HttpStatus status;

    public CourierRuntimeException(int code, String message) {
        this(BAD_REQUEST, code, message);
    }

    public CourierRuntimeException(String message) {
        super(message);
    }

    public CourierRuntimeException(HttpStatus status, int code, String message) {
        super(message);
        this.code = code;
        this.status = status;
    }
}