package br.com.kg.estoque.domain.movimento;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para a entidade {@link Movimento}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) básicos para a entidade Movimento,
 * herdados de {@link JpaRepository}. Nenhuma implementação customizada é necessária aqui
 * para as operações padrão. Futuras consultas específicas podem ser adicionadas.
 */
public interface MovimentoRepository extends JpaRepository<Movimento, Long>{

}
