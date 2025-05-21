package br.com.imersao_app.demo.exception;

import org.springframework.http.HttpStatus;

public class TokenException extends ApiErrorException {
    public TokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "TOKEN_ERROR");
    }
}