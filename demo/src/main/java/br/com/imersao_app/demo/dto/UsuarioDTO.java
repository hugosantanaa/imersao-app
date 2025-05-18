package br.com.imersao_app.demo.dto;

import br.com.imersao_app.demo.entity.Usuario;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;
    private List<EnderecoDTO> enderecos;

    public static UsuarioDTO fromEntity(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());

        if (usuario.getEnderecos() != null) {
            dto.setEnderecos(
                usuario.getEnderecos().stream()
                        .map(EnderecoDTO::fromEntity)
                        .collect(Collectors.toList())
            );
        }

        return dto;
    }
}