package br.com.kg.estoque.domain.material;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para a entidade {@link Material}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) básicos para a entidade Material,
 * herdados de {@link JpaRepository}. Nenhuma implementação customizada é necessária aqui
 * para as operações padrão. Futuras consultas específicas podem ser adicionadas.
 */
public interface MaterialRepository extends JpaRepository<Material, Long>{
    
    
}
