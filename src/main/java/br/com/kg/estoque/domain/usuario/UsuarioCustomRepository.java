package br.com.kg.estoque.domain.usuario;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.repository.CustomRepository;

/**
 * Repositório customizado para a entidade {@link Usuario}.
 * <p>
 * Esta classe pode ser estendida para incluir métodos de consulta complexos e personalizados
 * que não são fornecidos por padrão pelo Spring Data JPA. Atualmente, herda a funcionalidade
 * base do {@link br.com.kg.estoque.repository.CustomRepository}, que fornece
 * métodos genéricos para consultas dinâmicas usadas pelo DataTables.
 */
@Repository
public class UsuarioCustomRepository extends CustomRepository<Usuario>{

}