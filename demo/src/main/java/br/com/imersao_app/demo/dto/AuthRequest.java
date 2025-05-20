package br.com.imersao_app.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthRequest {
    private String nome;    // usado apenas no cadastro
    private String email;
    private String senha;
    private String role;

    public AuthRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
}