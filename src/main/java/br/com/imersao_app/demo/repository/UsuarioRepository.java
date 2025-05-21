package br.com.imersao_app.demo.repository;

import br.com.imersao_app.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String login);

    // Verifica se email existe (mais eficiente que findByEmail + isPresent)
    boolean existsByEmailIgnoreCase(String email);

    // Busca por email com endereços carregados em uma única query (evita N+1)
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.enderecos WHERE u.email = :email")
    Optional<Usuario> findByEmailWithEnderecos(@Param("email") String email);

    // Atualiza último login
    @Modifying
    @Query("UPDATE Usuario u SET u.ultimoLogin = :data WHERE u.id = :id")
    void atualizarUltimoLogin(@Param("id") Long id, @Param("data") LocalDateTime data);

    // Atualiza último logout
    @Modifying
    @Query("UPDATE Usuario u SET u.ultimoLogout = :data WHERE u.id = :id")
    void atualizarUltimoLogout(@Param("id") Long id, @Param("data") LocalDateTime data);

    // Busca usuários ativos/inativos
    long countByAtivoTrue();
    long countByAtivoFalse();
}