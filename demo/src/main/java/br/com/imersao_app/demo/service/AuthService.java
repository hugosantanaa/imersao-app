package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.dto.AuthRequest;
import br.com.imersao_app.demo.dto.AuthResponse;
import br.com.imersao_app.demo.entity.Role;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.exception.*;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getSenha()
                )
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();
            usuarioRepository.atualizarUltimoLogin(usuario.getId(), LocalDateTime.now());

            String token = jwtService.generateToken(usuario);
            String refreshToken = jwtService.generateRefreshToken(usuario);
            
            return buildAuthResponse(usuario, token, refreshToken);
        } catch (BadCredentialsException ex) {
            throw new AutenticacaoException("Credenciais inválidas");
        }
    }
    
    private Role validateRole(String role) {
        if (role == null || role.isEmpty()) {
            return Role.USUARIO; // Valor padrão
        }
        
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Role inválida: " + role);
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
        String token = jwtService.generateToken(usuarioSalvo);
        String refreshToken = jwtService.generateRefreshToken(usuarioSalvo);

        return AuthResponse.builder()
            .token(token) // Garanta que isso não é null
            .refreshToken(refreshToken)
            .type("Bearer")
            .email(usuarioSalvo.getEmail())
            .role(usuarioSalvo.getRole())
            .expiresIn(jwtService.getExpirationInMillis(token))
            .build();
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        try {
            if (!jwtService.isRefreshTokenValid(refreshToken)) {
                throw new TokenException("Refresh token inválido ou expirado");
            }

            String email = jwtService.extractUsername(refreshToken);
            return usuarioRepository.findByEmailIgnoreCase(email)
                .map(usuario -> {
                    String newToken = jwtService.generateToken(usuario);
                    return buildAuthResponse(usuario, newToken, refreshToken);
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        } catch (JwtException ex) {
            log.error("Erro ao processar refresh token: {}", ex.getMessage());
            throw new TokenException("Falha ao renovar token");
        }
    }

    private AuthResponse buildAuthResponse(Usuario usuario, String token, String refreshToken) {
        Date expiration = jwtService.extractExpiration(token);
        Long expiresIn = expiration != null ? expiration.getTime() : null;
        
        return AuthResponse.builder()
            .token(token)
            .refreshToken(refreshToken)
            .type("Bearer")
            .email(usuario.getEmail())
            .role(usuario.getRole())
            .expiresIn(expiresIn) // Agora pode ser null, mas não causará NPE
            .ultimoLogin(usuario.getUltimoLogin())
            .nome(usuario.getNome())
            .userId(usuario.getId())
            .build();
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        try {
            String email = jwtService.extractUsername(token);
            return usuarioRepository.findByEmailIgnoreCase(email)
                .map(usuario -> jwtService.isTokenValid(token, usuario))
                .orElse(false);
        } catch (JwtException ex) {
            log.warn("Token inválido: {}", ex.getMessage());
            return false;
        }
    }
 }