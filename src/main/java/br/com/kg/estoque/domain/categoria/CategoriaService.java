package br.com.kg.estoque.domain.categoria;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.kg.estoque.custom.DataTableRequest;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.custom.DataTableUtils;

/**
 * Serviço de negócios para a entidade {@link Categoria}.
 * Esta classe encapsula a lógica de negócios e a interação com os repositórios
 * relacionados à entidade Categoria.
 */
@Service
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;
	private final CategoriaCustomRepository categoriaCustomRepository;
	private final ModelMapper mapper;

	/**
	 * Constrói o serviço com as dependências de repositório necessárias.
	 *
	 * @param categoriaRepository       O repositório JPA padrão para operações CRUD.
	 * @param categoriaCustomRepository O repositório customizado para consultas dinâmicas.
	 */
	public CategoriaService(CategoriaRepository categoriaRepository, CategoriaCustomRepository categoriaCustomRepository, ModelMapper mapper) {
		this.categoriaRepository = categoriaRepository;
		this.categoriaCustomRepository = categoriaCustomRepository;
		this.mapper = mapper;
	}

	/**
	 * Salva ou atualiza uma entidade de categoria no banco de dados.
	 * 
	 * @param categoria A entidade {@link Categoria} a ser salva.
	 */
	public void salvar(Categoria categoria) {
		categoriaRepository.save(categoria);
	}

	/**
	 * Busca todas as categorias cadastradas, ordenadas pelo nome em ordem ascendente.
	 * 
	 * @return Uma {@link List} de todas as entidades {@link Categoria}.
	 */
	public List<Categoria> buscarTodos() {
		return categoriaRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
	}

	/**
	 * Busca uma categoria específica pelo seu identificador único.
	 * 
	 * @param idCategoria O ID da categoria a ser buscada.
	 * @return Um {@link Optional} contendo a {@link Categoria} se encontrada, ou vazio caso contrário.
	 */
	public Optional<Categoria> buscarPorId(Long idCategoria) {
		return categoriaRepository.findById(idCategoria);
	}

	/**
	 * Exclui uma categoria do banco de dados com base no seu ID.
	 * 
	 * @param idCategoria O ID da categoria a ser excluída.
	 */
	public void excluir(Long idCategoria) {
		categoriaRepository.deleteById(idCategoria);
	}

	/**
	 * Prepara os dados da categoria para serem exibidos em um componente DataTables.
	 * Este método lida com a busca, paginação e formatação dos dados conforme
	 * os parâmetros recebidos do DataTables.
	 * 
	 * @param params Os parâmetros da requisição do DataTables, contendo informações de busca, paginação e ordenação.
	 * @return Um objeto {@link DataTableResult} pronto para ser serializado em JSON e enviado ao cliente.
	 */
	public DataTableResult dataTableCategoria(DataTableRequest params) {

		DataTableUtils.parseParams(params);
		List<Categoria> categoriasList = categoriaCustomRepository.listEntitiesToDataTable(DataTableUtils.parseColumns(params), params, Categoria.class);
		Integer totalFiltrado = categoriaCustomRepository.totalEntitiesToDataTable(DataTableUtils.parseColumns(params), params.getSearch().getValue(), Categoria.class);

		DataTableResult dataTable = new DataTableResult();
		dataTable.setDraw(params.getDraw());
		dataTable.setRecordsTotal((int) categoriaRepository.count());
		dataTable.setRecordsFiltered(totalFiltrado);
		dataTable.setData(categoriasList.stream()
				.map(c -> mapper.map(c, CategoriaDTO.class)).toList());

		return dataTable;
	}
}
