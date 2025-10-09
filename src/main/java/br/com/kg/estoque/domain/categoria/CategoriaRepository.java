package br.com.kg.estoque.domain.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para a entidade {@link Categoria}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) básicos para a entidade Categoria,
 * herdados de {@link JpaRepository}. Nenhuma implementação customizada é necessária aqui
 * para as operações padrão.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
}
