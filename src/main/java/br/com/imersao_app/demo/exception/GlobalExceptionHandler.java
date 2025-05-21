package br.com.imersao_app.demo.exception;

import br.com.imersao_app.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(TokenException.class)
	public ResponseEntity<ErrorResponse> handleTokenException(TokenException ex) {
	    ErrorResponse response = ErrorResponse.builder()
	        .timestamp(LocalDateTime.now())
	        .status(HttpStatus.UNAUTHORIZED.value())
	        .error("Token Error")
	        .message(ex.getMessage())
	        .errorCode(ex.getErrorCode())
	        .build();
	    
	    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

    // Captura erros de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(f -> ErrorResponse.FieldError.builder()
                .field(f.getField())
                .message(f.getDefaultMessage())
                .build())
            .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad Request")
            .message("Erro de validação")
            .fieldErrors(fieldErrors)
            .build();

        return ResponseEntity.badRequest().body(response);
    }

    // Captura exceções customizadas
    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ErrorResponse> handleApiError(ApiErrorException ex) {
        ErrorResponse response = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(ex.getStatus().value())
            .error(ex.getStatus().getReasonPhrase())
            .message(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    // Captura erros de autenticação (Spring Security)
    @ExceptionHandler({AccessDeniedException.class, AutenticacaoException.class})
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception ex) {
        HttpStatus status = ex instanceof AutenticacaoException ? 
            HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;

        ErrorResponse response = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(ex.getMessage())
            .errorCode("ACCESS_DENIED")
            .build();

        return new ResponseEntity<>(response, status);
    }

    // Captura erros genéricos (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
        ErrorResponse response = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .message("Ocorreu um erro inesperado")
            .build();

        return ResponseEntity.internalServerError().body(response);
    }
}