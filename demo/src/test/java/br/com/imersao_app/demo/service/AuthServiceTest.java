package br.com.imersao_app.demo.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import br.com.imersao_app.demo.dto.AuthRequest;
import br.com.imersao_app.demo.dto.AuthResponse;
import br.com.imersao_app.demo.entity.Role;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRealizarLoginComSucesso() {
        // Arrange
        AuthRequest request = new AuthRequest("test@email.com", "senha123");
        Usuario usuarioMock = Usuario.builder()
            .email("test@email.com")
            .senha(passwordEncoder.encode("senha123"))
            .role(Role.USUARIO)
            .build();
        
        when(authenticationManager.authenticate(any()))
            .thenReturn(new UsernamePasswordAuthenticationToken(usuarioMock, null));
        
        when(jwtService.generateToken(any(Usuario.class)))
            .thenReturn("fake-jwt");
        
        when(jwtService.generateRefreshToken(any(Usuario.class)))
            .thenReturn("fake-refresh-token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt", response.getToken());
        assertEquals("fake-refresh-token", response.getRefreshToken());
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
            .email("novo@email.com")
            .role(Role.USUARIO)
            .build();

        // Configura os mocks
        when(usuarioRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);
        
        // ⚠️ Configuração ESSENCIAL para o JwtService:
        when(jwtService.generateToken(any(Usuario.class)))
            .thenReturn("token-gerado");
        when(jwtService.generateRefreshToken(any(Usuario.class)))
            .thenReturn("refresh-token-gerado");
        when(jwtService.getExpirationInMillis(anyString()))
            .thenReturn(System.currentTimeMillis() + 3600000L); // 1 hora em milissegundos

        // 2. Execução
        AuthResponse response = authService.register(request);

        // 3. Verificações
        assertNotNull(response);
        assertEquals("token-gerado", response.getToken()); // Agora deve passar
        assertEquals("novo@email.com", response.getEmail());
    }
}