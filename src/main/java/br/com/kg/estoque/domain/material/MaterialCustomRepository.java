package br.com.kg.estoque.domain.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * Repositório customizado para a entidade {@link Material}.
 * Fornece métodos para construir e executar consultas JPQL dinâmicas,
 * especialmente para atender aos requisitos de paginação, busca e ordenação do DataTables.
 */
@Repository
public class MaterialCustomRepository{

    @PersistenceContext
    protected EntityManager em;

    /**
     * Lista as entidades de Material para exibição em um DataTables.
     * A consulta é construída dinamicamente para incluir busca em várias colunas (com tratamento de acentos para Strings
     * e conversão para texto para campos numéricos), paginação e ordenação.
     *
     * @param colunas As colunas da tabela que devem ser consideradas na busca.
     * @param params  Os parâmetros da requisição do DataTables (draw, start, length, search, order).
     * @return Uma lista paginada e ordenada de entidades {@link Material} que correspondem aos critérios de busca.
     */
	public List<Material> listEntitiesToDataTable(String[] colunas, DataTableParams params) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append(" FROM Material f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id") || coluna.equals("valor") || coluna.equals("saldo")) {
                // Faz CAST de campos numéricos para string para a busca
                jpql.append(" OR CAST(f.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(f.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }
		
		jpql.append(" ) ");
		jpql.append(" ORDER BY ").append(colunas[params.getOrderCol()]).append(" ").append(params.getOrderDir());
		
		TypedQuery<Material> query = em.createQuery(jpql.toString(), Material.class);
		query.setParameter("searchValue", "%"+Auxiliar.removeAcentos(params.getSearchValue().toLowerCase())+"%");
		query.setFirstResult(params.getStart());
		query.setMaxResults(params.getLength());
		
		return query.getResultList();
	}

    /**
     * Calcula o número total de entidades de Material que correspondem aos critérios de busca do DataTables.
     * Este método é usado para a paginação, para informar ao DataTables o total de registros filtrados.
     *
     * @param colunas As colunas da tabela que devem ser consideradas na busca.
     * @param sSearch O valor de busca inserido pelo usuário.
     * @return O número total de registros que correspondem à busca.
     */
	public Long totalEntitiesToDataTable(String[] colunas, String sSearch) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(*) FROM Material f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id") || coluna.equals("valor") || coluna.equals("saldo")) {
                // Faz CAST de campos numéricos para string para a busca
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
