package br.com.kg.estoque.domain.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Repositório Spring Data JPA para a entidade {@link Usuario}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) básicos, herdados de {@link JpaRepository},
 * e também define consultas personalizadas para a entidade Usuario, baseadas no nome dos métodos.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	/**
     * Busca um usuário pelo seu login, ignorando a capitalização.
     * Retorna um {@link UserDetails} para integração com o Spring Security.
     *
     * @param login O login do usuário a ser buscado.
     * @return Um {@link Optional} contendo os detalhes do usuário ({@link UserDetails}) se encontrado, ou vazio caso contrário.
     */
	Optional<UserDetails> findByLoginIgnoreCase(String login);

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return Um {@link Optional} contendo a entidade {@link Usuario} se encontrada, ou vazio caso contrário.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca um usuário pelo e-mail, ignorando um usuário com um ID específico.
     * <p>
     * Este método é útil para validações durante a atualização de um usuário,
     * para verificar se o e-mail modificado já está em uso por outro usuário no sistema.
     *
     * @param email O e-mail a ser buscado.
     * @param id    O ID do usuário a ser excluído da busca.
     * @return Um {@link Optional} contendo o {@link Usuario} se um com o mesmo e-mail e ID diferente for encontrado.
     */
    Optional<Usuario> findByEmailAndIdIsNot(String email, Long id);

}
