package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.entity.Endereco;
import br.com.imersao_app.demo.repository.EnderecoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    public EnderecoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosEnderecos() {
        Pageable pageable = PageRequest.of(0, 10); // página 0, 10 itens por página

        when(enderecoRepository.findAll(pageable))
            .thenReturn(new PageImpl<>(Arrays.asList(new Endereco(), new Endereco())));

        assertEquals(2, enderecoService.listarTodos(pageable).getContent().size());
    }

    @Test
    void deveBuscarEnderecoPorId() {
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        Endereco resultado = enderecoService.buscarPorId(1L);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveSalvarEndereco() {
        Endereco endereco = new Endereco();
        when(enderecoRepository.save(any())).thenReturn(endereco);

        Endereco salvo = enderecoService.salvar(endereco);
        assertNotNull(salvo);
    }
}