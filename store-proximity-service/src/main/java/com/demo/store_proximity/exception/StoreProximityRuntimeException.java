package com.demo.store_proximity.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter

public class StoreProximityRuntimeException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	private int code;
	private HttpStatus status;

	public StoreProximityRuntimeException(int code, String message) {
		this(BAD_REQUEST, code, message);
	}

	public StoreProximityRuntimeException(String message) {
		super(message);
	}

	public StoreProximityRuntimeException(HttpStatus status, int code, String message) {
		super(message);
		this.code = code;
		this.status = status;
	}
}