package br.com.kg.estoque.domain.grupo_acesso;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;

/**
 * Serviço de negócios para a entidade {@link GrupoAcesso}.
 * Esta classe encapsula a lógica de negócios e a interação com os repositórios
 * para a entidade Grupo de Acesso.
 */
@Service
public class GrupoAcessoService {

	private final GrupoAcessoRepository grupoAcessoRepository;
	private final GrupoAcessoCustomRepository customGrupoAcessoRepository;

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
	public DataTableResult dataTableGrupoAcessos(DataTableParams params) {

		String[] colunas = {"id", "grupo", "situacao"};
		
		// List<GrupoAcesso> gruposList = customGrupoAcessoRepository.listEntitiesToDataTable(colunas, params, GrupoAcesso.class);
		// Long registrosFiltrados = customGrupoAcessoRepository.totalEntitiesToDataTable(colunas, Auxiliar.removeAcentos(params.getSearchValue()), GrupoAcesso.class);

		// dataTable.setDraw(params.getDraw());
		// dataTable.setRecordsTotal((int) grupoAcessoRepository.count());
		// dataTable.setRecordsFiltered(registrosFiltrados);
		// dataTable.setData(gruposList.stream().map(c -> new Object[]{c.getId(), c.getGrupo(), c.getSituacao(), c.getId()}).toList());
		DataTableResult dataTable = new DataTableResult();

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
