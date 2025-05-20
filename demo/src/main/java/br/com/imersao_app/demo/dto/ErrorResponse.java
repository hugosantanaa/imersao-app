package br.com.imersao_app.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String errorCode;
    private List<FieldError> fieldErrors;

    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}