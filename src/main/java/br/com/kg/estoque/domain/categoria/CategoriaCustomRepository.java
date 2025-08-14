package br.com.kg.estoque.domain.categoria;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.repository.CustomRepository;

@Repository
public class CategoriaCustomRepository extends CustomRepository<Categoria>{

    private Logger logger = Logger.getLogger(CategoriaCustomRepository.class.getName());
    



    public List<Categoria> listEntitiesToDataTableCategoria(String[] colunas, DataTableParams params) {


        logger.log(Level.ALL ,() -> "params: " + params);

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
        return query.getSingleResult();
    }


}
