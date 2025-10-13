package br.com.kg.estoque.custom;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

/**
 * Representa o objeto de resultado esperado pelo DataTables para popular a tabela.
 * Esta classe é serializada para JSON e enviada como resposta a uma requisição AJAX do DataTables.
 */
@JsonSerialize
public class DataTableResult {

	/**
	 * O contador de renderização da requisição original.
	 * Essencial para que o DataTables possa garantir a sincronia e segurança das requisições.
	 */
	@Getter @Setter
	@JsonProperty("draw")
	private String draw;

	/**
	 * O número total de registros no banco de dados, antes de qualquer filtro.
	 */
	@Getter @Setter
	@JsonProperty("recordsTotal")
	private Integer recordsTotal;

	/**
	 * O número de registros após a aplicação do filtro (busca).
	 * Se não houver busca, este valor deve ser igual ao {@code recordsTotal}.
	 */
	@Getter @Setter
	@JsonProperty("recordsFiltered")
	private Long recordsFiltered;

	/**
	 * A lista de dados a ser exibida na tabela.
	 * Cada elemento da lista é um array de objetos, onde cada objeto representa o valor de uma célula na linha.
	 */
	@Getter @Setter
	@JsonProperty("data")
	private List<Object[]> data;
}
