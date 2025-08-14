package br.com.kg.estoque.domain.fornecedor;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * Essa classe contém métodos customizados de acesso ao banco de dados.
 * Os recursos aqui descritos não são suportados por default pelo Spring Data
 * e dão maior controle as transações e filtros dinâmicos
 * @author Ailton
 * @version 2.0.1
 */
@Repository
public class FornecedorCustomRepositoryImpl implements FornecedorCustomRepository {
	
	@PersistenceContext
	private EntityManager em;

	/**
	 * lista os contatos para o CRUD dinâmico com Json do Data Table com base no cliente selecionado
	 */
	@Override
	public List<Fornecedor> listFornecedoresToDataTable(String[] colunas, DataTableParams params) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append(" FROM Fornecedor f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id")) {
                // Faz CAST do id direto pra string (sem UNACCENT)
                jpql.append(" OR CAST(f.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(f.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }
		
		jpql.append(" ) ");
		jpql.append(" ORDER BY "+ colunas[params.getOrderCol()]+ " " +  params.getOrderDir());
		
		TypedQuery<Fornecedor> query = em.createQuery(jpql.toString(), Fornecedor.class);
		query.setParameter("searchValue", "%"+Auxiliar.removeAcentos(params.getSearchValue().toLowerCase())+"%");
		query.setFirstResult(params.getStart());
		query.setMaxResults(params.getLength());
		
		return query.getResultList();

	}
	
	/**
	 * retorna o total de registros com paginação para o Json do DataTable
	 */
	@Override
	public Long totalFornecedoresToDataTable(String[] colunas, String sSearch) {
		
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT COUNT(*) FROM Fornecedor f WHERE f.id > 0 AND ( 1=0  ");
		
        for (String coluna : colunas) {
            if (coluna.equals("id")) {
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
