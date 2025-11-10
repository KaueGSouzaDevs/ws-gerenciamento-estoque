package br.com.kg.estoque.domain.material;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.kg.estoque.enuns.SituacaoMaterial;

/**
 * Repositório Spring Data JPA para a entidade {@link Material}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) básicos para a entidade Material,
 * herdados de {@link JpaRepository}. Nenhuma implementação customizada é necessária aqui
 * para as operações padrão. Futuras consultas específicas podem ser adicionadas.
 */
public interface MaterialRepository extends JpaRepository<Material, Long>{

    List<Material> findAllBySituacao(SituacaoMaterial ativo);

    @Query("SELECT COUNT(m) FROM Material m WHERE m.saldo < m.estoqueMinimo")
    Long getMateriaisEmEstoqueBaixo();

    @Query("SELECT SUM(m.precoCusto * m.saldo) FROM Material m")
    BigDecimal getValorTotalEstoque();

    @Query("""
        SELECT COUNT(m)
        FROM Material m
        WHERE m.saldo > 0
            AND m.situacao = :situacao
        """)
    Long getMateriaisEmEstoque(SituacaoMaterial situacao);
    
    
}
