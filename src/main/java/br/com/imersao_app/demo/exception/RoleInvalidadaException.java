package br.com.imersao_app.demo.exception;

import org.springframework.http.HttpStatus;

public class RoleInvalidadaException extends ApiErrorException {
	public RoleInvalidadaException (String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
}

