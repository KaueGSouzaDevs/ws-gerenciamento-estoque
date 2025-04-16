package br.com.kg.estoque.domain.categoria;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.repository.CustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class CategoriaCustomRepository extends CustomRepository<Categoria>{

    @PersistenceContext
    protected EntityManager em;
    



    public List<Categoria> listEntitiesToDataTableCategoria(String[] colunas, DataTableParams params) {

        StringBuilder jpql = new StringBuilder();

            //Pode acrescentar condicionais em sql
        jpql.append(" FROM Categoria t WHERE t.id > 0 ");
        jpql.append(" AND ( 1 = 0 ");

        

        for (String coluna : colunas) {
            jpql.append(" OR UNACCENT(LOWER(CAST(t.").append(coluna).append(" AS text))) LIKE :sSearch ");
        }
        

        jpql.append(" ) ");
        jpql.append(" ORDER BY %s %s".formatted(colunas[params.iSortCol_0()], params.sSortDir_0()));
        
        var query = em.createQuery(jpql.toString(), Categoria.class);

        query.setParameter("sSearch", "%"+Auxiliar.removeAcentos(params.sSearch().toLowerCase())+"%");
        query.setFirstResult(params.iDisplayStart());
        query.setMaxResults(params.iDisplayLength());
        
        return query.getResultList();
    }

    
    
    

    public Long totalEntitiesToDataTableCategoria(String[] colunas, String sSearch) {
        StringBuilder jpql = new StringBuilder();  

            //Pode acrescentar condicionais em sql
        jpql.append(" FROM Categoria t WHERE t.id > 0 ");
        jpql.append(" AND ( 1 = 0 ");

        for (String coluna : colunas) {
            jpql.append(" OR UNACCENT(LOWER(CAST(t.").append(coluna).append(" AS text))) LIKE :sSearch ");
        }

        jpql.append(" ) ");

        var query = em.createQuery(jpql.toString(), Long.class);

        query.setParameter("sSearch", "%"+Auxiliar.removeAcentos(sSearch.toLowerCase())+"%");
        System.out.println(query.getSingleResult());
        return (Long) query.getSingleResult();
    };


}
