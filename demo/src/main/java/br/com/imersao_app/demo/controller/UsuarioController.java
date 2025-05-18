package br.com.imersao_app.demo.controller;

import br.com.imersao_app.demo.dto.EnderecoDTO;
import br.com.imersao_app.demo.dto.UsuarioDTO;
import br.com.imersao_app.demo.dto.UsuarioFormDTO;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listarTodos(Pageable pageable) {
        Page<UsuarioDTO> page = usuarioService.listarTodos(pageable)
            .map(this::toDTO);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(usuario));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioFormDTO dto) {
//        Usuario usuario = fromDTO(dto);
//        usuario.setId(id); // <- Aqui está o que estava faltando
//        Usuario atualizado = usuarioService.atualizar(id, usuario);
//        return ResponseEntity.ok(toDTO(atualizado));
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setRole(usuario.getRole());

        if (usuario.getEnderecos() != null) {
            List<EnderecoDTO> enderecosDTO = usuario.getEnderecos().stream().map(endereco -> {
                EnderecoDTO eDto = new EnderecoDTO();
                eDto.setId(endereco.getId());
                eDto.setLogradouro(endereco.getLogradouro());
                eDto.setNumero(endereco.getNumero());
                eDto.setComplemento(endereco.getComplemento());
                eDto.setBairro(endereco.getBairro());
                eDto.setCidade(endereco.getCidade());
                eDto.setEstado(endereco.getEstado());
                eDto.setCep(endereco.getCep());
                return eDto;
            }).toList();

            dto.setEnderecos(enderecosDTO);
        }

        return dto;
    }

}