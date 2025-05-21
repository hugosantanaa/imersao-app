package br.com.imersao_app.demo.dto;

import lombok.Data;

@Data
public class EnderecoViaCepDTO {
	
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String cep;
}