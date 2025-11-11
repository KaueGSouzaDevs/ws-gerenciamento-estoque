package br.com.kg.estoque.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.kg.estoque.domain.usuario.Usuario;
import br.com.kg.estoque.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

/**
 * Serviço de autenticação que implementa a interface {@link UserDetailsService} do Spring Security.
 * É responsável por carregar os detalhes de um usuário a partir do banco de dados durante o processo de autenticação.
 */
@Repository
@Transactional
public class AutenticacaoService implements UserDetailsService{

	private final UsuarioRepository userRepository;

	/**
     * Constrói o serviço com a dependência do repositório de usuário.
     *
     * @param userRepository O repositório para acesso aos dados dos usuários.
     */
	public AutenticacaoService(UsuarioRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Localiza um usuário com base no seu nome de login (ignorando maiúsculas/minúsculas).
	 * Este é o método principal usado pelo Spring Security para obter os detalhes do usuário.
	 * 
	 * @param login O nome de usuário (login) cuja informação está sendo solicitada.
	 * @return Um objeto {@link UserDetails} contendo as informações do usuário (nunca {@code null}).
	 * @throws UsernameNotFoundException se o usuário não puder ser encontrado.
	 */
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
		Optional<Usuario> user = userRepository.findByNameIgnoreCase(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return user.get();
	}
}
