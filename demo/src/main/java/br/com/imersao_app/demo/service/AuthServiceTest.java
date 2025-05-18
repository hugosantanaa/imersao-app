package br.com.imersao_app.demo.service;


import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import br.com.imersao_app.demo.dto.AuthRequest;
import br.com.imersao_app.demo.dto.AuthResponse;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


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
        AuthRequest request = new AuthRequest("user@email.com", "123");

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setRole("USER");

        Authentication authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(usuario);

        when(authenticationManager.authenticate(any())).thenReturn(authMock);
        when(jwtService.generateToken(usuario)).thenReturn("fake-jwt");

        AuthResponse response = authService.login(request);

        assertEquals("fake-jwt", response.getToken());
        assertEquals("USER", response.getRole());
    }

    @Test
    void deveCadastrarNovoUsuario() {
        AuthRequest request = new AuthRequest("user@email.com", "123", "Nome", "USER");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        Usuario salvo = new Usuario();
        salvo.setEmail(request.getEmail());
        salvo.setRole("USER");

        when(passwordEncoder.encode("123")).thenReturn("hashed");
        when(usuarioRepository.save(any())).thenReturn(salvo);
        when(jwtService.generateToken(salvo)).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertEquals("token", response.getToken());
        assertEquals("USER", response.getRole());
    }