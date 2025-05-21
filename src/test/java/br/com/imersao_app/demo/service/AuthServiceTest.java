package br.com.imersao_app.demo.service;


import br.com.imersao_app.demo.config.JwtUtils;
import br.com.imersao_app.demo.config.UserDetailsImpl;
import br.com.imersao_app.demo.dto.JwtResponse;
import br.com.imersao_app.demo.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import br.com.imersao_app.demo.dto.AuthRequest;
import br.com.imersao_app.demo.dto.AuthResponse;
import br.com.imersao_app.demo.entity.Role;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRealizarAutenticarComSucesso() {
        // Arrange
        LoginRequest request = new LoginRequest("test@email.com", "senha123");
        Usuario usuarioMock = Usuario.builder()
            .id(1L)
            .email("test@email.com")
            .senha(passwordEncoder.encode("senha123"))
            .role(Role.USUARIO)
            .build();
        
        // Cria o UserDetailsImpl corretamente
        UserDetailsImpl userDetails = UserDetailsImpl.build(usuarioMock);
        
        // Cria a autenticação com UserDetailsImpl
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        
        when(authenticationManager.authenticate(any()))
            .thenReturn(authentication);
        
        when(jwtService.generateJwtToken(any(Authentication.class)))
            .thenReturn("fake-jwt");

        // Act
        ResponseEntity<JwtResponse> response = authService.autenticar(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt", response.getBody().token());
        assertEquals("test@email.com", response.getBody().email());
    }

    @Test
    void deveCadastrarNovoUsuario() {
        // 1. Configuração
        AuthRequest request = new AuthRequest(
            "Hugo",
            "novo@email.com", 
            "senha123",  
            "USUARIO"
        );
        
        Usuario usuarioMock = Usuario.builder()
            .id(1L)
            .nome("Hugo")
            .email("novo@email.com")
            .senha(passwordEncoder.encode("senha123"))
            .role(Role.USUARIO)
            .dataCriacao(LocalDateTime.now())
            .ativo(true)
            .build();

        UserDetailsImpl userDetails = UserDetailsImpl.build(usuarioMock);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, 
            null, 
            userDetails.getAuthorities()
        );

        // Configura os mocks
        when(usuarioRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senha-criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);
        when(authenticationManager.authenticate(any()))
            .thenReturn(authentication);
        when(jwtService.generateJwtToken(any(Authentication.class)))
            .thenReturn("token-gerado");

        // 2. Execução
        AuthResponse response = authService.register(request);

        // 3. Verificações
        assertEquals("token-gerado", response.getToken());
        assertEquals("novo@email.com", response.getEmail());
        assertEquals("[ROLE_USUARIO]", response.getRole());
    }
}