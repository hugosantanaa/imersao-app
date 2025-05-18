package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.entity.Endereco;
import br.com.imersao_app.demo.repository.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;

	public Endereco salvar(Endereco endereco) {
		return enderecoRepository.save(endereco);
	}

	public Page<Endereco> listarTodos(Pageable pageable) {
	    return enderecoRepository.findAll(pageable);
	}

	public Endereco buscarPorId(Long id) {
		return enderecoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));
	}

	public Endereco atualizar(Long id, Endereco novo) {
		Endereco atual = buscarPorId(id);
		atual.setLogradouro(novo.getLogradouro());
		atual.setNumero(novo.getNumero());
		atual.setComplemento(novo.getComplemento());
		atual.setBairro(novo.getBairro());
		atual.setCidade(novo.getCidade());
		atual.setEstado(novo.getEstado());
		atual.setCep(novo.getCep());
		return enderecoRepository.save(atual);
	}

	public void deletar(Long id) {
		enderecoRepository.deleteById(id);
	}
}
