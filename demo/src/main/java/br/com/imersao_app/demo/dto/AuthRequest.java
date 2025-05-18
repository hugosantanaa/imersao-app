package br.com.imersao_app.demo.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String nome;    // usado apenas no cadastro
    private String email;
    private String senha;
    private String role;
}