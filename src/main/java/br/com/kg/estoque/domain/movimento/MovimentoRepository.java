package br.com.kg.estoque.domain.movimento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.kg.estoque.enuns.TipoMovimento;

/**
 * Repositório Spring Data JPA para a entidade {@link Movimento}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) básicos para a entidade Movimento,
 * herdados de {@link JpaRepository}. Nenhuma implementação customizada é necessária aqui
 * para as operações padrão. Futuras consultas específicas podem ser adicionadas.
 */
public interface MovimentoRepository extends JpaRepository<Movimento, Long>{

    @Query("""
        SELECT SUM(m2.precoVenda * m.quantidade)
        FROM Movimento m
        INNER JOIN m.material m2
        WHERE m.dataMovimento >= :dataInicioMes
            AND m.dataMovimento <= :dataFimMes
            AND m.tipoMovimento = :tipo
    """)
    BigDecimal getValorSaidasMes(LocalDateTime dataInicioMes, LocalDateTime dataFimMes, TipoMovimento tipo);

}
