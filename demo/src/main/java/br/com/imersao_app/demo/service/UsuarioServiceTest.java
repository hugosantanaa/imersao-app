package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import java.util.Optional;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    public UsuarioServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(new Usuario(), new Usuario()));
        assertEquals(2, usuarioService.listarTodos().size());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveSalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setSenha("123");
        when(passwordEncoder.encode("123")).thenReturn("hashed");
        when(usuarioRepository.save(any())).thenReturn(usuario);

        Usuario salvo = usuarioService.salvar(usuario);

        verify(passwordEncoder).encode("123");
        assertNotNull(salvo);
    }
}