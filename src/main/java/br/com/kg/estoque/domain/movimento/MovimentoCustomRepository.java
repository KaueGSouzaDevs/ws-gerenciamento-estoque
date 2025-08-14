package br.com.kg.estoque.domain.movimento;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.repository.CustomRepository;
import jakarta.persistence.TypedQuery;

@Repository
public class MovimentoCustomRepository extends CustomRepository<Movimento>{
    
    /**
	 * lista os contatos para o CRUD dinâmico com Json do Data Table com base no cliente selecionado
	 */

	public List<Movimento> listMovimentosToDataTable(String[] colunas, DataTableParams params) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append(" FROM Movimento f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id") || coluna.equals("data") || coluna.equals("quantidade")) {
                // Faz CAST do id direto pra string (sem UNACCENT)
                jpql.append(" OR CAST(f.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(f.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }
		
		jpql.append(" ) ");
		jpql.append(" ORDER BY "+ colunas[params.getOrderCol()]+ " " +  params.getOrderDir());
		
		TypedQuery<Movimento> query = em.createQuery(jpql.toString(), Movimento.class);
		query.setParameter("searchValue", "%"+Auxiliar.removeAcentos(params.getSearchValue().toLowerCase())+"%");
		query.setFirstResult(params.getStart());
		query.setMaxResults(params.getLength());
		
		return query.getResultList();

	}
	
	/**
	 * retorna o total de registros com paginação para o Json do DataTable
	 */
	public Long totalMovimentosToDataTable(String[] colunas, String sSearch) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(*) FROM Movimento f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id") || coluna.equals("data") || coluna.equals("quantidade")) {
                // Faz CAST do id direto pra string (sem UNACCENT)
                jpql.append(" OR CAST(f.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(f.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }
		
		jpql.append(" ) ");
		
		var query = em.createQuery(jpql.toString(), Long.class);
		query.setParameter("searchValue", "%"+sSearch.toLowerCase()+"%");
		
		return query.getSingleResult();
		
	}
}
