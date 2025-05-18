package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.dto.AuthRequest;
import br.com.imersao_app.demo.dto.AuthResponse;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder codificadorDeSenha;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager autenticador;

    public AuthResponse login(AuthRequest request) {
        try {
            Authentication autenticacao = autenticador.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );

            Usuario usuario = (Usuario) autenticacao.getPrincipal();
            String token = jwtService.generateToken(usuario);

            return new AuthResponse(token, usuario.getEmail(), usuario.getRole());

        } catch (Exception ex) {
            throw new RuntimeException("Email ou senha inválidos.");
        }
    }

    public AuthResponse register(AuthRequest request) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(request.getEmail());
        if (existente.isPresent()) {
            throw new RuntimeException("Email já cadastrado!");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.getNome());
        novoUsuario.setEmail(request.getEmail());
        novoUsuario.setSenha(codificadorDeSenha.encode(request.getSenha()));
        novoUsuario.setRole(request.getRole().toUpperCase()); // 👈 seta a role enviada

        Usuario salvo = usuarioRepository.save(novoUsuario);
        String token = jwtService.generateToken(salvo);

        return new AuthResponse(token, salvo.getEmail(), salvo.getRole());
    }
}