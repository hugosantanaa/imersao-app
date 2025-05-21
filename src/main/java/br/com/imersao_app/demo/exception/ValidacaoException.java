package br.com.imersao_app.demo.exception;

import org.springframework.http.HttpStatus;

public class ValidacaoException extends ApiErrorException {
    public ValidacaoException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
}

