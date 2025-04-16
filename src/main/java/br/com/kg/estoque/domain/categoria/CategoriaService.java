package br.com.kg.estoque.domain.categoria;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private CategoriaCustomRepository categoriaCustomRepository;

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

	public DataTableResult dataTableCategoria(DataTableParams params) {

		System.out.println("a");
		System.out.println("params: " + params);
		
		// colunas a serem consultadas conforme modelos relacionais
		String[] colunas={"id","nome","situacao", "id"};
		
		System.out.println("b");
		// varre a lista de registros no banco de dados e adiciona na lista de informações
		var categoriasList = categoriaCustomRepository.listEntitiesToDataTableCategoria(colunas, params);
		System.out.println("c");
		
		// gera o DataTable e popula com as informações da lista de objetos
		DataTableResult dataTable = new DataTableResult();
		System.out.println("d");
		dataTable.setSEcho(String.valueOf(System.currentTimeMillis()));
		System.out.println("e");
		dataTable.setITotalRecords(categoriasList.size());
		System.out.println("f");
		
		dataTable.setITotalDisplayRecords(categoriaCustomRepository.totalEntitiesToDataTableCategoria(colunas, Auxiliar.removeAcentos(params.sSearch())));
		System.out.println("g");
		
		dataTable.setAaData(categoriasList.toArray());
		System.out.println("h");
		return dataTable;
	}

}
