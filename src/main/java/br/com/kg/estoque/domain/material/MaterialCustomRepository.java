package br.com.kg.estoque.domain.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class MaterialCustomRepository{


    @PersistenceContext
    protected EntityManager em;



	public List<Material> listEntitiesToDataTable(String[] colunas, DataTableParams params) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append(" FROM Material f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id") || coluna.equals("valor") || coluna.equals("saldo")) {
                // Faz CAST do id direto pra string (sem UNACCENT)
                jpql.append(" OR CAST(f.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(f.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }
		
		jpql.append(" ) ");
		jpql.append(" ORDER BY "+ colunas[params.getOrderCol()]+ " " +  params.getOrderDir());
		
		TypedQuery<Material> query = em.createQuery(jpql.toString(), Material.class);
		query.setParameter("searchValue", "%"+Auxiliar.removeAcentos(params.getSearchValue().toLowerCase())+"%");
		query.setFirstResult(params.getStart());
		query.setMaxResults(params.getLength());
		
		return query.getResultList();

	}



	public Long totalEntitiesToDataTable(String[] colunas, String sSearch) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(*) FROM Material f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id") || coluna.equals("valor") || coluna.equals("saldo")) {
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
