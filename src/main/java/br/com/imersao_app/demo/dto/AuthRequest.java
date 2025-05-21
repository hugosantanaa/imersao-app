package br.com.imersao_app.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.imersao_app.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthRequest {
	@JsonProperty("nome")
    private String nome;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("senha")
    private String senha;
    
    @JsonProperty("role")
    private String role;

//    public AuthRequest(String email, String senha, String string, String string2) {
//        this.email = email;
//        this.senha = senha;
//    }
}