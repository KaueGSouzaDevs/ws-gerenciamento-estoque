package br.com.kg.estoque.domain.grupo_acesso;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;

@Service
public class GrupoAcessoService {

	private GrupoAcessoRepository grupoAcessoRepository;
	private GrupoAcessoCustomRepository customGrupoAcessoRepository;

	public GrupoAcessoService(GrupoAcessoRepository grupoAcessoRepository, GrupoAcessoCustomRepository customGrupoAcessoRepository) {
		this.grupoAcessoRepository = grupoAcessoRepository;
		this.customGrupoAcessoRepository = customGrupoAcessoRepository;
	}



	/**
	 * Gera um DataTableResult para o CRUD de grupos de acesso, com base nos parâmetros
	 * de configuração do DataTable.
	 * 
	 * @param params Os parâmetros de configuração do DataTable.
	 * @return Um DataTableResult contendo as informações para o CRUD de grupos de acesso.
	 */
	public DataTableResult dataTableGrupoAcessos(DataTableParams params) {

		// colunas a serem consultadas conforme modelos relacionais
		String[] colunas={"id","situacao","grupo"};
		
		// varre a lista de registros no banco de dados e adiciona na lista de informações
		List<GrupoAcesso> gruposList = customGrupoAcessoRepository.listEntitiesToDataTable(colunas, params, GrupoAcesso.class);
		Long registrosFiltrados = customGrupoAcessoRepository.totalEntitiesToDataTable(colunas, Auxiliar.removeAcentos(params.getSearchValue()), GrupoAcesso.class);

		// gera o DataTable e popula com as informações da lista de objetos
		DataTableResult dataTable = new DataTableResult();
		dataTable.setDraw(String.valueOf(System.currentTimeMillis()));
		dataTable.setRecordsTotal(gruposList.size());
		dataTable.setRecordsFiltered(registrosFiltrados);
		dataTable.setData(gruposList.stream().map(c -> new Object[]{c.getId(), c.getSituacao(), c.getGrupo()}).toList());
		return dataTable;
	}



	/**
	 * Encontra um GrupoAcesso pelo seu id.
	 * 
	 * @param idGrupoAcesso O id do GrupoAcesso a ser encontrado.
	 * @return Um Optional contendo o GrupoAcesso encontrado, ou um Optional vazio caso ele não seja encontrado.
	 */
	public Optional<GrupoAcesso> findById(Long idGrupoAcesso) {
		return grupoAcessoRepository.findById(idGrupoAcesso);
	}



	/**
	 * Salva um GrupoAcesso no banco de dados. Se o GrupoAcesso
	 * tiver um id nulo, ele será inserido no banco de dados, caso
	 * contrário, ele será atualizado no banco de dados.
	 * 
	 * @param grupoAcesso O GrupoAcesso a ser salvo.
	 */
	public void save(GrupoAcesso grupoAcesso) {
		grupoAcessoRepository.save(grupoAcesso);
		
	}



	/**
	 * Retorna uma lista de todos os GruposAcesso cadastrados.
	 * Caso apenasAtivos seja true, apenas os GruposAcesso com status Ativo
	 * ser o retornados.
	 * 
	 * @param apenasAtivos Se true, apenas os GruposAcesso com status Ativo
	 * ser o retornados.
	 * @return Uma lista de GruposAcesso.
	 */
	public List<GrupoAcesso> findAll(boolean apenasAtivos){
		
		if(apenasAtivos) {
			return grupoAcessoRepository.findAllAtivos();
		}
		return grupoAcessoRepository.findAll(); 
	}

}
