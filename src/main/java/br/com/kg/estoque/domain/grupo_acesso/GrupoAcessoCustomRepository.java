package br.com.kg.estoque.domain.grupo_acesso;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.repository.CustomRepository;

/**
 * Repositório customizado para a entidade {@link GrupoAcesso}.
 * <p>
 * Esta classe pode ser estendida para incluir métodos de consulta complexos e personalizados
 * que não são fornecidos por padrão pelo Spring Data JPA. Atualmente, herda a funcionalidade
 * base do {@link br.com.kg.estoque.repository.CustomRepository}.
 */
@Repository
public class GrupoAcessoCustomRepository extends CustomRepository<GrupoAcesso> {

}
