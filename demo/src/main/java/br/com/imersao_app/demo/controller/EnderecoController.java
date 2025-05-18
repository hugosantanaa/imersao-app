package br.com.imersao_app.demo.controller;

import br.com.imersao_app.demo.dto.EnderecoDTO;
import br.com.imersao_app.demo.dto.EnderecoFormDTO;
import br.com.imersao_app.demo.entity.Endereco;
import br.com.imersao_app.demo.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<EnderecoDTO> criar(@Valid @RequestBody EnderecoFormDTO dto) {
        Endereco endereco = fromDTO(dto);
        Endereco salvo = enderecoService.salvar(endereco);
        return ResponseEntity.ok(toDTO(salvo));
    }

    @GetMapping
    public ResponseEntity<Page<EnderecoDTO>> listarTodos(Pageable pageable) {
        Page<EnderecoDTO> page = enderecoService.listarTodos(pageable)
            .map(this::toDTO);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> buscarPorId(@PathVariable Long id) {
        Endereco endereco = enderecoService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(endereco));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EnderecoFormDTO dto) {
        Endereco endereco = fromDTO(dto);
        Endereco atualizado = enderecoService.atualizar(id, endereco);
        return ResponseEntity.ok(toDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EnderecoDTO toDTO(Endereco endereco) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(endereco.getId());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCidade(endereco.getCidade());
        dto.setEstado(endereco.getEstado());
        dto.setCep(endereco.getCep());
        return dto;
    }

    private Endereco fromDTO(EnderecoFormDTO dto) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        return endereco;
    }
}