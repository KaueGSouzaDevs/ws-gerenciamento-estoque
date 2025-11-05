package br.com.kg.estoque.domain.movimento;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.repository.CustomRepository;
import jakarta.persistence.TypedQuery;

/**
 * Repositório customizado para a entidade {@link Movimento}.
 * Fornece métodos para construir e executar consultas JPQL dinâmicas,
 * especialmente para atender aos requisitos de paginação, busca e ordenação do DataTables.
 */
@Repository
public class MovimentoCustomRepository extends CustomRepository<Movimento>{
    
    /**
     * Lista as entidades de Movimento para exibição em um DataTables.
     * A consulta é construída dinamicamente para incluir busca em várias colunas, paginação e ordenação.
     *
     * @param colunas As colunas da tabela que devem ser consideradas na busca.
     * @param params  Os parâmetros da requisição do DataTables (draw, start, length, search, order).
     * @return Uma lista paginada e ordenada de entidades {@link Movimento} que correspondem aos critérios de busca.
     */
	public List<Movimento> listMovimentosToDataTable(String[] colunas, DataTableParams params) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append(" FROM Movimento f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id") || coluna.equals("dataMovimento") || coluna.equals("quantidade")) {
                // Faz CAST de campos numéricos/data para string para a busca
                jpql.append(" OR CAST(f.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(f.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }
		
		jpql.append(" ) ");
		jpql.append(" ORDER BY ").append(colunas[params.getOrderCol()]).append(" ").append(params.getOrderDir());
		
		TypedQuery<Movimento> query = em.createQuery(jpql.toString(), Movimento.class);
		query.setParameter("searchValue", "%"+Auxiliar.removeAcentos(params.getSearchValue().toLowerCase())+"%");
		query.setFirstResult(params.getStart());
		query.setMaxResults(params.getLength());
		
		return query.getResultList();
	}
	
	/**
     * Calcula o número total de entidades de Movimento que correspondem aos critérios de busca do DataTables.
     *
     * @param colunas As colunas da tabela que devem ser consideradas na busca.
     * @param sSearch O valor de busca inserido pelo usuário.
     * @return O número total de registros que correspondem à busca.
     */
	public Long totalMovimentosToDataTable(String[] colunas, String sSearch) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(*) FROM Movimento f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id") || coluna.equals("dataMovimento") || coluna.equals("quantidade")) {
                // Faz CAST de campos numéricos/data para string para a busca
                jpql.append(" OR CAST(f.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(f.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }
		
		jpql.append(" ) ");
		
		var query = em.createQuery(jpql.toString(), Long.class);
		query.setParameter("searchValue", "%"+Auxiliar.removeAcentos(sSearch.toLowerCase())+"%");
		
		return query.getSingleResult();
	}
}
