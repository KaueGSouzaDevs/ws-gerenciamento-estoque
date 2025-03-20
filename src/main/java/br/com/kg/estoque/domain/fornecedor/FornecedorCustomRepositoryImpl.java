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
			jpql.append(" OR UNACCENT(LOWER(CAST(").append(coluna).append(" AS text))) LIKE :sSearch ");
		}
		
		jpql.append(" ) ");
		jpql.append(" ORDER BY "+ colunas[params.iSortCol_0()]+ " " +  params.sSortDir_0());
		
		TypedQuery<Fornecedor> query = em.createQuery(jpql.toString(), Fornecedor.class);
		query.setParameter("sSearch", "%"+Auxiliar.removeAcentos(params.sSearch().toLowerCase())+"%");
		query.setFirstResult(params.iDisplayStart());
		query.setMaxResults(params.iDisplayLength());
		
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
			jpql.append(" OR UNACCENT(LOWER(CAST(").append(coluna).append(" AS text))) LIKE :sSearch ");
		}
		
		jpql.append(" ) ");
		
		var query = em.createQuery(jpql.toString(), Long.class);
		query.setParameter("sSearch", "%"+sSearch.toLowerCase()+"%");
		
		return (Long) query.getSingleResult();
		
	}

}
