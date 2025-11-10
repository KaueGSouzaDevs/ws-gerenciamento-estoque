package br.com.kg.estoque.domain.grupo_acesso;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableRequest;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.custom.DataTableUtils;

/**
 * Serviço de negócios para a entidade {@link GrupoAcesso}.
 * Esta classe encapsula a lógica de negócios e a interação com os repositórios
 * para a entidade Grupo de Acesso.
 */
@Service
public class GrupoAcessoService {

	private final GrupoAcessoRepository grupoAcessoRepository;
	private final GrupoAcessoCustomRepository customGrupoAcessoRepository;

	@Autowired
	private ModelMapper mapper;

	/**
     * Constrói o serviço com as dependências de repositório necessárias.
     *
     * @param grupoAcessoRepository O repositório JPA padrão para operações CRUD.
     * @param customGrupoAcessoRepository O repositório customizado para consultas dinâmicas.
     */
	public GrupoAcessoService(GrupoAcessoRepository grupoAcessoRepository, GrupoAcessoCustomRepository customGrupoAcessoRepository) {
		this.grupoAcessoRepository = grupoAcessoRepository;
		this.customGrupoAcessoRepository = customGrupoAcessoRepository;
	}

	/**
     * Prepara os dados do grupo de acesso para serem exibidos em um componente DataTables.
     *
     * @param params Os parâmetros da requisição do DataTables, contendo informações de busca, paginação e ordenação.
     * @return Um objeto {@link DataTableResult} pronto para ser serializado em JSON e enviado ao cliente.
     */
	public DataTableResult dataTableGrupoAcessos(DataTableRequest params) {

		
		DataTableUtils.parseParams(params);
		List<GrupoAcesso> gruposList = customGrupoAcessoRepository.listEntitiesToDataTable(DataTableUtils.parseColumns(params), params, GrupoAcesso.class);
		Integer registrosFiltrados = customGrupoAcessoRepository.totalEntitiesToDataTable(DataTableUtils.parseColumns(params), Auxiliar.removeAcentos(params.getSearch().getValue()), GrupoAcesso.class);
		
		DataTableResult dataTable = new DataTableResult();
		dataTable.setDraw(params.getDraw());
		dataTable.setRecordsTotal((int) grupoAcessoRepository.count());
		dataTable.setRecordsFiltered(registrosFiltrados);
		dataTable.setData(gruposList.stream().map(c -> mapper.map(c, GrupoAcessoDTO.class)).toList());

		return dataTable;
	}

	/**
     * Busca um Grupo de Acesso específico pelo seu identificador único.
     *
     * @param idGrupoAcesso O ID do Grupo de Acesso a ser encontrado.
     * @return Um {@link Optional} contendo o {@link GrupoAcesso} se encontrado, ou um Optional vazio caso contrário.
     */
	public Optional<GrupoAcesso> findById(Long idGrupoAcesso) {
		return grupoAcessoRepository.findById(idGrupoAcesso);
	}

	/**
     * Salva ou atualiza uma entidade de Grupo de Acesso no banco de dados.
     *
     * @param grupoAcesso O {@link GrupoAcesso} a ser salvo.
     */
	public void save(GrupoAcesso grupoAcesso) {
		grupoAcessoRepository.save(grupoAcesso);
	}

	/**
     * Retorna uma lista de todos os Grupos de Acesso cadastrados.
     * Permite filtrar para retornar apenas os grupos ativos.
     *
     * @param apenasAtivos Se `true`, retorna apenas os grupos com situação "Ativo".
     * @return Uma {@link List} de entidades {@link GrupoAcesso}.
     */
	public List<GrupoAcesso> findAll(boolean apenasAtivos){
		
		if(apenasAtivos) {
			return grupoAcessoRepository.findAllAtivos();
		}
		return grupoAcessoRepository.findAll(); 
	}

    public Optional<GrupoAcesso> buscarPorId(Long id) {
		return grupoAcessoRepository.findById(id);
    }

    public void excluir(Long id) {
        grupoAcessoRepository.deleteById(id);
    }
}
