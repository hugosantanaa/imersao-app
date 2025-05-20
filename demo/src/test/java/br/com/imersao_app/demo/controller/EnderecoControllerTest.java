package br.com.imersao_app.demo.controller;

import br.com.imersao_app.demo.dto.EnderecoDTO;
import br.com.imersao_app.demo.dto.EnderecoFormDTO;
import br.com.imersao_app.demo.entity.Endereco;
import br.com.imersao_app.demo.service.EnderecoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoControllerTest {

    @Mock
    private EnderecoService enderecoService;

    @InjectMocks
    private EnderecoController enderecoController;

    @Test
    void criar_DeveRetornarEnderecoDTOCriado() {
        // Arrange
        EnderecoFormDTO dto = new EnderecoFormDTO();
        dto.setLogradouro("Rua Teste");
        dto.setNumero("123");
        dto.setCep("12345678");
        
        Endereco enderecoSalvo = new Endereco();
        enderecoSalvo.setId(1L);
        enderecoSalvo.setLogradouro(dto.getLogradouro());
        
        when(enderecoService.salvar(any(Endereco.class))).thenReturn(enderecoSalvo);

        // Act
        ResponseEntity<EnderecoDTO> response = enderecoController.criar(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(enderecoSalvo.getId(), response.getBody().getId());
        assertEquals(dto.getLogradouro(), response.getBody().getLogradouro());
        verify(enderecoService).salvar(any(Endereco.class));
    }

    @Test
    void listarTodos_DeveRetornarPageDeEnderecos() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        Page<Endereco> page = new PageImpl<>(List.of(endereco));
        
        when(enderecoService.listarTodos(pageable)).thenReturn(page);

        // Act
        ResponseEntity<Page<EnderecoDTO>> response = enderecoController.listarTodos(pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(enderecoService).listarTodos(pageable);
    }

    @Test
    void buscarPorId_DeveRetornarEnderecoQuandoExistir() {
        // Arrange
        Long id = 1L;
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setLogradouro("Av. Principal");
        
        when(enderecoService.buscarPorId(id)).thenReturn(endereco);

        // Act
        ResponseEntity<EnderecoDTO> response = enderecoController.buscarPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("Av. Principal", response.getBody().getLogradouro());
        verify(enderecoService).buscarPorId(id);
    }

    @Test
    void atualizar_DeveRetornarEnderecoAtualizado() {
        // Arrange
        Long id = 1L;
        EnderecoFormDTO dto = new EnderecoFormDTO();
        dto.setLogradouro("Rua Atualizada");
        dto.setNumero("456");
        
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setId(id);
        enderecoAtualizado.setLogradouro(dto.getLogradouro());
        
        when(enderecoService.atualizar(eq(id), any(Endereco.class))).thenReturn(enderecoAtualizado);

        // Act
        ResponseEntity<EnderecoDTO> response = enderecoController.atualizar(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals(dto.getLogradouro(), response.getBody().getLogradouro());
        verify(enderecoService).atualizar(eq(id), any(Endereco.class));
    }

    @Test
    void deletar_DeveRetornarNoContent() {
        // Arrange
        Long id = 1L;
        doNothing().when(enderecoService).deletar(id);

        // Act
        ResponseEntity<Void> response = enderecoController.deletar(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(enderecoService).deletar(id);
    }

    @Test
    void toDTO_DeveConverterEnderecoParaDTO() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 101");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01001000");

        // Act
        EnderecoDTO dto = enderecoController.toDTO(endereco);

        // Assert
        assertEquals(endereco.getId(), dto.getId());
        assertEquals(endereco.getLogradouro(), dto.getLogradouro());
        assertEquals(endereco.getNumero(), dto.getNumero());
        assertEquals(endereco.getComplemento(), dto.getComplemento());
        assertEquals(endereco.getBairro(), dto.getBairro());
        assertEquals(endereco.getCidade(), dto.getCidade());
        assertEquals(endereco.getEstado(), dto.getEstado());
        assertEquals(endereco.getCep(), dto.getCep());
    }

    @Test
    void fromDTO_DeveConverterEnderecoFormDTOParaEndereco() {
        // Arrange
        EnderecoFormDTO dto = new EnderecoFormDTO();
        dto.setLogradouro("Av. Brasil");
        dto.setNumero("1000");
        dto.setComplemento("Sala 2");
        dto.setBairro("Jardins");
        dto.setCidade("Rio de Janeiro");
        dto.setEstado("RJ");
        dto.setCep("20040000");

        // Act
        Endereco endereco = enderecoController.fromDTO(dto);

        // Assert
        assertEquals(dto.getLogradouro(), endereco.getLogradouro());
        assertEquals(dto.getNumero(), endereco.getNumero());
        assertEquals(dto.getComplemento(), endereco.getComplemento());
        assertEquals(dto.getBairro(), endereco.getBairro());
        assertEquals(dto.getCidade(), endereco.getCidade());
        assertEquals(dto.getEstado(), endereco.getEstado());
        assertEquals(dto.getCep(), endereco.getCep());
    }
}