package br.com.kg.estoque.repository;

import br.com.kg.estoque.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para a entidade {@link Tenant}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) para a entidade Tenant.
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

}
