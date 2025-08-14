package br.com.kg.estoque.domain.categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;

/**
 * Classe de serviço para a entidade Categoria.
 */
@Service
public class CategoriaService {

	private CategoriaRepository categoriaRepository;
	private CategoriaCustomRepository categoriaCustomRepository;

	public CategoriaService(CategoriaRepository categoriaRepository, CategoriaCustomRepository categoriaCustomRepository) {
		this.categoriaRepository = categoriaRepository;
		this.categoriaCustomRepository = categoriaCustomRepository;
	}

	/**
	 * Salva uma categoria.
	 * 
	 * @param categoria a categoria a ser salva
	 */
	public void salvar(Categoria categoria) {
		categoriaRepository.save(categoria);
	}

	/**
	 * Retorna uma lista de todas as categorias.
	 * 
	 * @return a lista de categorias
	 */
	public List<Categoria> buscarTodos() {
		return categoriaRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
	}

	/**
	 * Retorna uma categoria pelo seu ID.
	 * 
	 * @param idCategoria o ID da categoria
	 * @return a categoria encontrada, ou vazio se não encontrada
	 */
	public Optional<Categoria> buscarPorId(Long idCategoria) {
		return categoriaRepository.findById(idCategoria);
	}

	/**
	 * Exclui uma categoria pelo seu ID.
	 * 
	 * @param idCategoria o ID da categoria a ser excluída
	 */
	public void excluir(Long idCategoria) {
		categoriaRepository.deleteById(idCategoria);
	}



	/**
	 * Gera json dinâmico para Data Table (CRUD)
	 * 
	 * @param draw        o valor do draw do DataTable
	 * @param start       o valor do start do DataTable
	 * @param length      o valor do length do DataTable
	 * @param searchValue o valor do searchValue do DataTable
	 * @param orderCol    o valor do orderCol do DataTable
	 * @param orderDir    o valor do orderDir do DataTable
	 * @return o objeto DataTableResult com os dados para o DataTable
	 */

	public DataTableResult dataTableCategoria(DataTableParams params) {
		
		// colunas a serem consultadas conforme modelos relacionais
		String[] colunas={"id","nome","situacao"};

		var categoriasList = categoriaCustomRepository.listEntitiesToDataTableCategoria(colunas, params);
		long totalFiltrado = categoriaCustomRepository.totalEntitiesToDataTableCategoria(colunas, Auxiliar.removeAcentos(params.getSearchValue()));

		
		// gera o DataTable e popula com as informações da lista de objetos
		DataTableResult dataTable = new DataTableResult();

		// Gera o draw do DataTable
		dataTable.setDraw(String.valueOf(System.currentTimeMillis()));

		// Gera a quantidade de registros totais no DataTable
		dataTable.setRecordsTotal(categoriasList.size());

		// Gera a quantidade de registros filtrados no DataTable
		dataTable.setRecordsFiltered(totalFiltrado);
		
		//Gera a lista de dados para serem populados no DataTable
		dataTable.setData(categoriasList.stream()
							.map(c -> new Object[]{
								c.getId(), 
								c.getNome(), 
								c.getSituacao(), 
								c.getId()
							}).toList());
		return dataTable;
	}

}
