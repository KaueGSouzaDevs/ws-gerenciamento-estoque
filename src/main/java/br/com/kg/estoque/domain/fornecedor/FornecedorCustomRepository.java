package br.com.kg.estoque.domain.fornecedor;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.repository.CustomRepository;

/**
 * Interface para o repositório customizado da entidade {@link Fornecedor}.
 * Define os métodos para consultas dinâmicas que não são cobertas pela interface padrão do Spring Data JPA,
 * como as necessárias para a integração com o DataTables.
 */
@Repository
public class FornecedorCustomRepository extends CustomRepository<Fornecedor> {

}
