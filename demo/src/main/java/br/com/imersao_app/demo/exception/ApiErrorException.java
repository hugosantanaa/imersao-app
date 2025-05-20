package br.com.imersao_app.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    public ApiErrorException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}

// Subclasses para tipos específicos:


