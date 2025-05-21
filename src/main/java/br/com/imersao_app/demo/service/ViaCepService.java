package br.com.imersao_app.demo.service;

import br.com.imersao_app.demo.dto.EnderecoViaCepDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ViaCepService {

    private static final String URL_VIACEP = "https://viacep.com.br/ws/{cep}/json/";

    public EnderecoViaCepDTO consultarEnderecoPorCep(String cep) {
        log.info("Consultando o endereço para o CEP: {}", cep);
        RestTemplate restTemplate = new RestTemplate();
        EnderecoViaCepDTO endereco = restTemplate.getForObject(URL_VIACEP, EnderecoViaCepDTO.class, cep);
        log.info("Endereço encontrado: {}", endereco);
        return endereco;
    }
}