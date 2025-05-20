package br.com.imersao_app.demo.dto;

import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private Role role;  // Alterado de String para enum Role
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime ultimoLogin;
    private LocalDateTime ultimoLogout;
    private List<EnderecoDTO> enderecos;

    public static UsuarioDTO fromEntity(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .ativo(usuario.getAtivo())
                .dataCriacao(usuario.getDataCriacao())
                .ultimoLogin(usuario.getUltimoLogin())
                .ultimoLogout(usuario.getUltimoLogout())
                .enderecos(usuario.getEnderecos() != null ? 
                        usuario.getEnderecos().stream()
                                .map(EnderecoDTO::fromEntity)
                                .collect(Collectors.toList()) : 
                        null)
                .build();
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .role(dto.getRole())
                .ativo(dto.getAtivo())
                .dataCriacao(dto.getDataCriacao())
                .ultimoLogin(dto.getUltimoLogin())
                .ultimoLogout(dto.getUltimoLogout())
                .build();
    }
}