package br.com.kg.estoque.domain.grupo_acesso;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repositório Spring Data JPA para a entidade {@link GrupoAcesso}.
 * <p>
 * Fornece métodos CRUD (Create, Read, Update, Delete) básicos, herdados de {@link JpaRepository},
 * e também define consultas personalizadas para a entidade GrupoAcesso.
 */
public interface GrupoAcessoRepository extends JpaRepository<GrupoAcesso, Long> {
	
	/**
	 * Busca todos os grupos de acesso que estão com a situação "Ativo".
	 * A consulta é definida explicitamente usando a anotação {@link Query}.
	 *
	 * @return Uma {@link List} de entidades {@link GrupoAcesso} ativas.
	 */
	@Query("from GrupoAcesso g WHERE g.situacao = 'Ativo'")
	List<GrupoAcesso> findAllAtivos();
}
