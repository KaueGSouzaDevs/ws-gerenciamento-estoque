package br.com.kg.estoque.domain.material;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.repository.CustomRepository;

/**
 * Repositório customizado para a entidade {@link Material}.
 * Fornece métodos para construir e executar consultas JPQL dinâmicas,
 * especialmente para atender aos requisitos de paginação, busca e ordenação do DataTables.
 */
@Repository
public class MaterialCustomRepository extends CustomRepository<Material>{

}
