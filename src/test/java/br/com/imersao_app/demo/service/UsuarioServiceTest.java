package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveSalvarUsuarioComSenhaCriptografada() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");
        usuario.setEmail("test@email.com");
        
        when(passwordEncoder.encode(anyString())).thenReturn("senha-criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        // Assert
        assertNotNull(usuarioSalvo);
        assertEquals("senha-criptografada", usuarioSalvo.getSenha());
        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveListarTodosUsuariosComPaginacao() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        Usuario usuario = new Usuario();
        Page<Usuario> page = new PageImpl<>(List.of(usuario));
        
        when(usuarioRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Usuario> resultado = usuarioService.listarTodos(pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(usuarioRepository).findAll(pageable);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        // Arrange
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = usuarioService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoPorId() {
        // Arrange
        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.buscarPorId(id);
        });
    }

    @Test
    void deveAtualizarUsuario() {
        // Arrange
        Long id = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setSenha("senha-antiga");
        
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("Novo Nome");
        usuarioAtualizado.setEmail("novo@email.com");
        usuarioAtualizado.setSenha("nova-senha");
        
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.encode(anyString())).thenReturn("senha-criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        // Act
        Usuario resultado = usuarioService.atualizar(id, usuarioAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Novo Nome", resultado.getNome());
        assertEquals("novo@email.com", resultado.getEmail());
        verify(passwordEncoder).encode("nova-senha");
    }

    @Test
    void naoDeveAtualizarSenhaQuandoVazia() {
        // Arrange
        Long id = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setSenha("senha-antiga");
        
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("Novo Nome");
        usuarioAtualizado.setEmail("novo@email.com");
        usuarioAtualizado.setSenha("");
        
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        // Act
        Usuario resultado = usuarioService.atualizar(id, usuarioAtualizado);

        // Assert
        assertEquals("senha-antiga", resultado.getSenha());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deveDeletarUsuario() {
        // Arrange
        Long id = 1L;
        doNothing().when(usuarioRepository).deleteById(id);

        // Act
        usuarioService.deletar(id);

        // Assert
        verify(usuarioRepository).deleteById(id);
    }

    @Test
    void deveCarregarUserDetailsPorEmail() {
        // Arrange
        String email = "user@email.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        
        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = usuarioService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void deveLancarExcecaoQuandoEmailNaoEncontrado() {
        // Arrange
        String email = "inexistente@email.com";
        when(usuarioRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            usuarioService.loadUserByUsername(email);
        });
    }
}