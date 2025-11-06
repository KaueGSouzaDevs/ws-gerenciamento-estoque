package br.com.kg.estoque.domain.movimento;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.repository.CustomRepository;

/**
 * Repositório customizado para a entidade {@link Movimento}.
 * Fornece métodos para construir e executar consultas JPQL dinâmicas,
 * especialmente para atender aos requisitos de paginação, busca e ordenação do DataTables.
 */
@Repository
public class MovimentoCustomRepository extends CustomRepository<Movimento>{
}
