package br.com.imersao_app.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Corpo de entrada para autenticação")
public class LoginRequest {
    @Schema(description = "Login do usuário") private String email;
    @Schema(description = "Senha do usuário") private String senha;
}
