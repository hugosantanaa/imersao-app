package br.com.imersao_app.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UsuarioFormDTO {
    private String nome;
    private String email;
    private String senha;
    private String role;
}