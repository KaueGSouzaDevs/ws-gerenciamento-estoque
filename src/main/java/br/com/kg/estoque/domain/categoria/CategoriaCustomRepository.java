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


        System.out.println("params: " + params);

        StringBuilder jpql = new StringBuilder();

            //Pode acrescentar condicionais em sql
        jpql.append(" FROM Categoria t WHERE t.id > 0 ");
        jpql.append(" AND ( 1 = 0 ");

        

        for (String coluna : colunas) {
            if (coluna.equals("id")) {
                // Faz CAST do id direto pra string (sem UNACCENT)
                jpql.append(" OR CAST(t.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(t.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }
        

        jpql.append(" ) ");


        jpql.append(" ORDER BY %s %s".formatted(colunas[params.getOrderCol()], params.getOrderDir()));

        var query = em.createQuery(jpql.toString(), Categoria.class);
        query.setParameter("searchValue", "%"+Auxiliar.removeAcentos(params.getSearchValue().toLowerCase())+"%");
        query.setFirstResult(params.getStart());
        query.setMaxResults(params.getLength());
        
        return query.getResultList();
    }

    
    
    

    public Long totalEntitiesToDataTableCategoria(String[] colunas, String searchValue) {
        StringBuilder jpql = new StringBuilder();  

        //Pode acrescentar condicionais em sql
        jpql.append("SELECT COUNT(*) FROM Categoria t WHERE t.id > 0");
        jpql.append(" AND ( 1 = 0 ");

        for (String coluna : colunas) {
            if (coluna.equals("id")) {
                // Faz CAST do id direto pra string (sem UNACCENT)
                jpql.append(" OR CAST(t.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent
                jpql.append(" OR LOWER(CAST(UNACCENT(t.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }

        jpql.append(" ) ");

        var query = em.createQuery(jpql.toString(), Long.class);
        query.setParameter("searchValue", "%"+Auxiliar.removeAcentos(searchValue.toLowerCase())+"%");
        return (Long) query.getSingleResult();
    };


}
