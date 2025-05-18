package br.com.imersao_app.demo.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import br.com.imersao_app.demo.entity.Usuario;
import br.com.imersao_app.demo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public Usuario salvar(Usuario usuario) {
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}

	public Page<Usuario> listarTodos(Pageable pageable) {
	    return usuarioRepository.findAll(pageable);
	}

	public Usuario buscarPorId(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
	}

	public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
		Usuario usuario = buscarPorId(id);
		usuario.setNome(usuarioAtualizado.getNome());
		usuario.setEmail(usuarioAtualizado.getEmail());

		if (!usuarioAtualizado.getSenha().isBlank()) {
			usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
		}
		return usuarioRepository.save(usuario);
	}

	public void deletar(Long id) {
		usuarioRepository.deleteById(id);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail: " + email));
	}
}