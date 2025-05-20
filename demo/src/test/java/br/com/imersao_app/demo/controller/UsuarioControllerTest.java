package br.com.imersao_app.demo.controller;

import br.com.imersao_app.demo.dto.EnderecoDTO;
import br.com.imersao_app.demo.dto.UsuarioDTO;
import br.com.imersao_app.demo.dto.UsuarioFormDTO;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.service.UsuarioService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void listarTodos_DeveRetornarPageDeUsuarios() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Page<Usuario> page = new PageImpl<>(List.of(usuario));
        
        when(usuarioService.listarTodos(pageable)).thenReturn(page);

        // Act
        ResponseEntity<Page<UsuarioDTO>> response = usuarioController.listarTodos(pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(usuarioService).listarTodos(pageable);
    }

    @Test
    void buscarPorId_DeveRetornarUsuarioQuandoExistir() {
        // Arrange
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        
        when(usuarioService.buscarPorId(id)).thenReturn(usuario);

        // Act
        ResponseEntity<UsuarioDTO> response = usuarioController.buscarPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        verify(usuarioService).buscarPorId(id);
    }

    @Test
    void atualizar_DeveRetornarUsuarioAtualizado() {
        // Arrange
        Long id = 1L;
        UsuarioFormDTO dto = new UsuarioFormDTO();
        dto.setNome("Novo Nome");
        dto.setEmail("novo@email.com");
        dto.setSenha("novaSenha");
        
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(id);
        usuarioAtualizado.setNome(dto.getNome());
        usuarioAtualizado.setEmail(dto.getEmail());
        
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(usuarioService.atualizar(eq(id), any(Usuario.class))).thenReturn(usuarioAtualizado);

        // Act
        ResponseEntity<UsuarioDTO> response = usuarioController.atualizar(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(dto.getNome(), response.getBody().getNome());
        assertEquals(dto.getEmail(), response.getBody().getEmail());
        verify(usuarioService).atualizar(eq(id), any(Usuario.class));
        verify(passwordEncoder).encode(dto.getSenha());
    }

    @Test
    void deletar_DeveRetornarNoContent() {
        // Arrange
        Long id = 1L;
        doNothing().when(usuarioService).deletar(id);

        // Act
        ResponseEntity<Void> response = usuarioController.deletar(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioService).deletar(id);
    }

    @Test
    void toDTO_DeveConverterUsuarioParaDTO() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setRole("USUARIO");

        // Act
        UsuarioDTO dto = usuarioController.toDTO(usuario);

        // Assert
        assertEquals(usuario.getId(), dto.getId());
        assertEquals(usuario.getNome(), dto.getNome());
        assertEquals(usuario.getEmail(), dto.getEmail());
        assertEquals(usuario.getRole(), dto.getRole());
    }

    @Test
    void toDTO_DeveConverterEnderecosQuandoPresentes() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        // Adicione endereços ao usuário se necessário

        // Act
        UsuarioDTO dto = usuarioController.toDTO(usuario);

        // Assert
        assertNotNull(dto);
        // Adicione asserts para endereços se necessário
    }

    @Test
    void fromDTO_DeveConverterFormDTOParaUsuarioComSenhaCriptografada() {
        // Arrange
        UsuarioFormDTO dto = new UsuarioFormDTO();
        dto.setNome("Teste");
        dto.setEmail("teste@email.com");
        dto.setSenha("senha123");
        
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("senhaCriptografada");

        // Act
        Usuario usuario = usuarioController.fromDTO(dto);

        // Assert
        assertEquals(dto.getNome(), usuario.getNome());
        assertEquals(dto.getEmail(), usuario.getEmail());
        assertEquals("senhaCriptografada", usuario.getSenha());
        verify(passwordEncoder).encode(dto.getSenha());
    }
}