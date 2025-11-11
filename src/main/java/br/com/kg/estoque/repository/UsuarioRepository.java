package br.com.kg.estoque.repository;

import br.com.kg.estoque.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade {@link Usuario}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) para a entidade Usuario.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndIdIsNot(String email, Long id);

    Optional<Usuario> findByNameIgnoreCase(String name);
}
