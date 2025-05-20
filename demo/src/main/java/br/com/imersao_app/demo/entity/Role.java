package br.com.imersao_app.demo.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USUARIO,
    ADMINISTRADOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}