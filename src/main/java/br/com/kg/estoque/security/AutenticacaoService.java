package br.com.kg.estoque.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.kg.estoque.domain.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class AutenticacaoService implements UserDetailsService{

	private UsuarioRepository userRepository;

	public AutenticacaoService(UsuarioRepository userRepository) {
		this.userRepository = userRepository;
	}



	/**
	 * Busca um usuário pelo login informado.
	 * 
	 * @param login - login do usuário
	 * @return - retorna o objeto do tipo UserDetails com as informações do
	 *         usuário
	 * @throws UsernameNotFoundException - caso o usuário não seja encontrado
	 */
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
		Optional<UserDetails> user = userRepository.findByLoginIgnoreCase(login);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return user.get();
	}

}
