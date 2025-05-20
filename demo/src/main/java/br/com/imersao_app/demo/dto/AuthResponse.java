package br.com.imersao_app.demo.dto;

import br.com.imersao_app.demo.entity.Role;
import br.com.imersao_app.demo.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;          // Token JWT gerado
    private String refreshToken; // Campo adicionado
    private String type;           // Tipo do token (ex: "Bearer")
    private String email;          // Email do usuário autenticado
    private Role role;             // Perfil do usuário (Enum: ADMIN, USUARIO)
    private Long expiresIn;        // Tempo de expiração em milissegundos (timestamp)
    private LocalDateTime ultimoLogin; // Opcional: último login (se necessário para o frontend)
    private String nome;           // Opcional: nome do usuário (útil para exibição no frontend)
    private Long userId;

    // Método estático para construção simplificada (usado no AuthService)
    public static AuthResponse of(Usuario usuario, String token, String refreshToken, Long expiresIn) {
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken) // Agora recebido como parâmetro
                .type("Bearer")
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .expiresIn(expiresIn)
                .ultimoLogin(usuario.getUltimoLogin())
                .nome(usuario.getNome())
                .userId(usuario.getId())
                .build();
    }
}

