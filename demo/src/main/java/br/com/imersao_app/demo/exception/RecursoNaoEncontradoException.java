package br.com.imersao_app.demo.exception;

import org.springframework.http.HttpStatus;

public class RecursoNaoEncontradoException extends ApiErrorException {
public RecursoNaoEncontradoException(String message) {
  super(message, HttpStatus.NOT_FOUND, "NOT_FOUND");
}
}