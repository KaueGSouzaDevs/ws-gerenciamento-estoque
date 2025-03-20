package br.com.kg.estoque.domain.fornecedor;

import java.util.List;

import br.com.kg.estoque.custom.DataTableParams;

public interface FornecedorCustomRepository {
	
	// Fornecedor findByAttribute(String attribute, String value);
	
	List<Fornecedor> listFornecedoresToDataTable(String[] colunas, DataTableParams params);
	
	Long totalFornecedoresToDataTable(String[] colunas, String sSearch);

}
