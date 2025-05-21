package br.com.imersao_app.demo.exception;

import org.springframework.http.HttpStatus;

public class AutenticacaoException extends ApiErrorException {
public AutenticacaoException(String message) {
  super(message, HttpStatus.UNAUTHORIZED, "AUTH_ERROR");
}
}
