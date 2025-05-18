package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.entity.Endereco;
import br.com.imersao_app.demo.repository.EnderecoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        when(enderecoRepository.findAll()).thenReturn(Arrays.asList(new Endereco(), new Endereco()));
        assertEquals(2, enderecoService.listarTodos().size());
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