package br.com.kg.estoque.domain.usuario;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.repository.UsuarioRepository;

/**
 * Serviço de negócios para a entidade {@link Usuario}.
 * Esta classe encapsula a lógica de negócios, validações e interação com os repositórios
 * para a entidade Usuário.
 */
@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper mapper;

	/**
     * Constrói o serviço com as dependências de repositório necessárias.
     *
     * @param usuarioRepository       O repositório JPA padrão para operações CRUD.
     */
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository; 
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
     * @param name O login do usuário a ser buscado.
     * @return Um {@link Optional} contendo os detalhes do usuário ({@link UserDetails}) se encontrado.
     */
    public Optional<Usuario> findByName(String name) {
        return usuarioRepository.findByNameIgnoreCase(name);
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

    public void gerarLogin(Usuario usuario) {
		String nomeCompleto = Auxiliar.removeAcentos(usuario.getName().trim());
		String[] nomes = nomeCompleto.split(" ");

		String loginBase = nomes[0].toLowerCase() + "." + nomes[nomes.length - 1].toLowerCase();
		String loginFinal =  loginBase;

		int i = 1;

		while (findByName(loginFinal).isPresent()) {
			loginFinal = loginBase + i;
			i++;
		}

		usuario.setName(loginFinal);
    }

	public Optional<Usuario> findByEmailAndIdNot(String email, Long id) {
		return usuarioRepository.findByEmailAndIdIsNot(email, id);
	}
}
