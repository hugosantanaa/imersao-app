package br.com.imersao_app.demo.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USUARIO,
    ADMINISTRADOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }

	public String toUpperCase() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
    
//    authService.validateRole(role.name());
}