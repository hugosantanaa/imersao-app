package br.com.imersao_app.demo.dto;

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
    private String token;         
    private String refreshToken;
    private String type;           
    private String email;    
    private String role;      
    private Long expiresIn;   
    private LocalDateTime ultimoLogin;
    private String nome;
    private Long userId;

    public static AuthResponse of(Usuario usuario, String token, String refreshToken, Long expiresIn) {
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .type("Bearer")
                .email(usuario.getEmail())
                .role(usuario.getRole().toString())
                .expiresIn(expiresIn)
                .ultimoLogin(usuario.getUltimoLogin())
                .nome(usuario.getNome())
                .userId(usuario.getId())
                .build();
    }
}

