package br.com.imersao_app.demo.config;

import br.com.imersao_app.demo.dto.UsuarioDTO;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;

    public UsuarioDTO getUsuarioLogado(HttpServletRequest request) throws AuthException {
        String token = parseJwt(request);
        if (token != null && jwtUtils.validateJwtToken(token)) {
            String email = jwtUtils.getEmailFromJwtToken(token);
            return getUsuarioLogadoByEmail(email);
        }
        throw new AuthException("Nenhum usuário logado encontrado.");
    }

    private UsuarioDTO getUsuarioLogadoByEmail(String email) {
        final Usuario usuarioEntidade = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return UsuarioDTO.builder()
                .id(usuarioEntidade.getId())
                .nome(usuarioEntidade.getNome())
                .email(usuarioEntidade.getEmail())
                .build();
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}