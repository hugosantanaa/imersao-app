package br.com.imersao_app.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.CONFLICT) // Retorna HTTP 409 Conflict
public class EmailExistenteException extends RuntimeException {
    
    public EmailExistenteException(String message) {
        super(message);
    }
    
    public EmailExistenteException(String message, Throwable cause) {
        super(message, cause);
    }
}