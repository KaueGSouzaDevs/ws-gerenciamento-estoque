package br.com.kg.estoque.domain.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;

/**
 * Serviço de negócios para a entidade {@link Usuario}.
 * Esta classe encapsula a lógica de negócios, validações e interação com os repositórios
 * para a entidade Usuário.
 */
@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final UsuarioCustomRepository customUsuarioRepository;

	/**
     * Constrói o serviço com as dependências de repositório necessárias.
     *
     * @param usuarioRepository       O repositório JPA padrão para operações CRUD.
     * @param customUsuarioRepository O repositório customizado para consultas dinâmicas.
     */
	public UsuarioService(UsuarioRepository usuarioRepository, UsuarioCustomRepository customUsuarioRepository) {
		this.usuarioRepository = usuarioRepository;
		this.customUsuarioRepository = customUsuarioRepository;
	}

	/**
     * Salva ou atualiza uma entidade de usuário no banco de dados.
     *
     * @param usuario O objeto {@link Usuario} a ser salvo.
     * @return A entidade {@link Usuario} salva, agora com o ID atribuído (se for uma nova entidade).
     */
	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	/**
     * Prepara os dados do usuário para serem exibidos em um componente DataTables.
     *
     * @param params Os parâmetros da requisição do DataTables (busca, paginação, ordenação).
     * @return Um objeto {@link DataTableResult} pronto para ser serializado em JSON.
     */
	public DataTableResult dataTableUsuarios(DataTableParams params) {

		String[] colunas = {"id", "nome", "login", "email", "situacaoUsuario"};
		
		List<Usuario> usuariosList = customUsuarioRepository.listEntitiesToDataTable(colunas, params, Usuario.class); 
		Long registrosFiltrados = customUsuarioRepository.totalEntitiesToDataTable(colunas, Auxiliar.removeAcentos(params.getSearchValue()), Usuario.class);
		
		DataTableResult dataTable = new DataTableResult();
		dataTable.setDraw(params.getDraw());
		dataTable.setRecordsTotal((int) usuarioRepository.count());
		dataTable.setRecordsFiltered(registrosFiltrados);
		dataTable.setData(usuariosList.stream().map(c -> new Object[]{c.getId(), c.getNome(), c.getLogin(), c.getEmail(), c.getSituacaoUsuario().getDescricao(), c.getId()}).toList());

		return dataTable;
	}

	/**
     * Busca um usuário pelo seu identificador único.
     *
     * @param idUsuario O ID do usuário a ser encontrado.
     * @return Um {@link Optional} contendo o {@link Usuario} se encontrado, ou vazio caso contrário.
     */
	public Optional<Usuario> findById(Long idUsuario) {
		return usuarioRepository.findById(idUsuario);
	}

	/**
     * Valida se o e-mail de um novo usuário já existe em outro cadastro.
     * Adiciona um erro ao {@link BindingResult} se a validação falhar.
     *
     * @param usuario O novo usuário a ser validado.
     * @param result  O objeto BindingResult para registrar erros de validação.
     */
	public void validaInclusao(Usuario usuario, BindingResult result) {
		if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
			result.rejectValue("email", "erroDescricao", "* Já existe um usuário cadastrado com este e-mail");
		}
	}
	
	/**
     * Valida se o e-mail de um usuário sendo alterado já existe em outro cadastro.
     * Adiciona um erro ao {@link BindingResult} se a validação falhar.
     *
     * @param usuario O usuário com os dados atualizados.
     * @param result  O objeto BindingResult para registrar erros de validação.
     */
	public void validaEdicao(Usuario usuario, BindingResult result) {
		if(usuarioRepository.findByEmailAndIdIsNot(usuario.getEmail(), usuario.getId()).isPresent()) {
			result.rejectValue("email", "erroDescricao", "* Já existe um usuário cadastrado com este e-mail");
		}
	}

    /**
     * Busca um usuário pelo seu login (ignorando maiúsculas/minúsculas).
     *
     * @param login O login do usuário a ser buscado.
     * @return Um {@link Optional} contendo os detalhes do usuário ({@link UserDetails}) se encontrado.
     */
    public Optional<UserDetails> findByLogin(String login) {
        return usuarioRepository.findByLoginIgnoreCase(login);
    }

	/**
     * Busca um usuário pelo seu endereço de e-mail.
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return Um {@link Optional} contendo a entidade {@link Usuario} se encontrada.
     */
	public Optional<Usuario> findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	/**
	 * Exclui um usuário pelo seu ID.
	 *
	 * @param id O ID do usuário a ser excluído.
	 */
	public void deleteById(Long id) {
		usuarioRepository.deleteById(id);
	}
}
