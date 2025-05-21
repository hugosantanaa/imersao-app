package br.com.imersao_app.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Resposta com token JWT e dados do usuário autenticado")
public record JwtResponse(
        @Schema(description = "Token JWT gerado") String token,
        @Schema(description = "Tipo do token, geralmente 'Bearer'") String type,
        @Schema(description = "ID do usuário") Long id,
        @Schema(description = "Nome de usuário") String username,
        @Schema(description = "Email do usuário") String email,
        @Schema(description = "Role do Usuario") String role
) {
    public JwtResponse(String token, Long id, String username, String email, String role) {
        this(token, "Bearer", id, username, email, role);
    }
}
