package br.com.imersao_app.demo.repository;

import br.com.imersao_app.demo.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
      }