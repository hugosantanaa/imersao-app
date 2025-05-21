package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.config.JwtUtils;
import br.com.imersao_app.demo.config.UserDetailsImpl;
import br.com.imersao_app.demo.dto.AuthRequest;
import br.com.imersao_app.demo.dto.AuthResponse;
import br.com.imersao_app.demo.dto.JwtResponse;
import br.com.imersao_app.demo.dto.LoginRequest;
import br.com.imersao_app.demo.entity.Role;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.exception.*;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtService;

    @Transactional
    public ResponseEntity<JwtResponse> autenticar(LoginRequest loginRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwt = jwtService.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities().toString()));
    }
    
    private Role validateRole(String string) {
        if (string == null || string.isEmpty()) {
            return Role.USUARIO; // Valor padrão
        }
        
        try {
            return Role.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Role inválida: " + string);
        }
    }

    @Transactional
    public AuthResponse register(AuthRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new EmailExistenteException("Email já está em uso");
        }

        Usuario novoUsuario = Usuario.builder()
            .nome(request.getNome())
            .email(request.getEmail().toLowerCase())
            .senha(passwordEncoder.encode(request.getSenha()))
            .role(validateRole(request.getRole()))
            .dataCriacao(LocalDateTime.now())
            .ativo(true)
            .build();

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // Gera o token e refresh token
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioSalvo.getEmail(), usuarioSalvo.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String jwt = jwtService.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return AuthResponse.builder()
            .token(jwt) // Garanta que isso não é null
            .type("Bearer")
            .email(userDetails.getEmail())
            .role(userDetails.getAuthorities().toString())
            .build();
    }
 }