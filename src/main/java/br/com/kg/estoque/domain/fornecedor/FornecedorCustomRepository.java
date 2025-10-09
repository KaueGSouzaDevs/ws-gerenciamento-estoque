package br.com.kg.estoque.domain.fornecedor;

import java.util.List;

import br.com.kg.estoque.custom.DataTableParams;

/**
 * Interface para o repositório customizado da entidade {@link Fornecedor}.
 * Define os métodos para consultas dinâmicas que não são cobertas pela interface padrão do Spring Data JPA,
 * como as necessárias para a integração com o DataTables.
 */
public interface FornecedorCustomRepository {

	/**
	 * Lista as entidades de Fornecedor para exibição em um DataTables.
	 * A implementação deste método deve construir uma consulta dinâmica para incluir busca,
	 * paginação e ordenação com base nos parâmetros fornecidos.
	 *
	 * @param colunas As colunas da tabela que devem ser consideradas na busca.
	 * @param params  Os parâmetros da requisição do DataTables (draw, start, length, search, order).
	 * @return Uma lista paginada e ordenada de entidades {@link Fornecedor} que correspondem aos critérios de busca.
	 */
	List<Fornecedor> listFornecedoresToDataTable(String[] colunas, DataTableParams params);

	/**
	 * Calcula o número total de entidades de Fornecedor que correspondem aos critérios de busca do DataTables.
	 * Este método é usado para a paginação, para informar ao DataTables o total de registros filtrados.
	 *
	 * @param colunas As colunas da tabela que devem ser consideradas na busca.
	 * @param sSearch O valor de busca inserido pelo usuário.
	 * @return O número total de registros que correspondem à busca.
	 */
	Long totalFornecedoresToDataTable(String[] colunas, String sSearch);

}
