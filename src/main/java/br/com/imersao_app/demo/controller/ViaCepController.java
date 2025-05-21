package br.com.imersao_app.demo.controller;

import br.com.imersao_app.demo.dto.EnderecoViaCepDTO;
import br.com.imersao_app.demo.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ViaCepController {

    private final ViaCepService viaCepService;

    @Autowired
    public ViaCepController(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("/consultar-cep/{cep}")
    public EnderecoViaCepDTO consultarCep(@PathVariable String cep) {
        return viaCepService.consultarEnderecoPorCep(cep);
    }
}
