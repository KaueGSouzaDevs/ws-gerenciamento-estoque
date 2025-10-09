package br.com.kg.estoque.domain.fornecedor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para a entidade {@link Fornecedor}.
 * <p>
 * Estende {@link JpaRepository} para fornecer operações CRUD padrão e
 * {@link FornecedorCustomRepository} para métodos de consulta personalizados.
 * Também inclui métodos de consulta derivados do nome para buscas específicas.
 */
@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long>, FornecedorCustomRepository{

    /**
     * Busca um fornecedor pelo seu CNPJ.
     *
     * @param cnpj O CNPJ do fornecedor a ser buscado.
     * @return Um {@link Optional} contendo o {@link Fornecedor} se encontrado, ou vazio caso contrário.
     */
    Optional<Fornecedor> findByCnpj(String cnpj);

    /**
     * Busca um fornecedor pelo CNPJ, ignorando um fornecedor com um ID específico.
     * <p>
     * Este método é útil para validações durante a atualização de um fornecedor,
     * para verificar se o CNPJ modificado já está em uso por outro fornecedor no sistema.
     *
     * @param cnpj O CNPJ a ser buscado.
     * @param id   O ID do fornecedor a ser excluído da busca.
     * @return Um {@link Optional} contendo o {@link Fornecedor} se um com o mesmo CNPJ e ID diferente for encontrado.
     */
    Optional<Fornecedor> findByCnpjAndIdNot(String cnpj, Long id);
    
}
