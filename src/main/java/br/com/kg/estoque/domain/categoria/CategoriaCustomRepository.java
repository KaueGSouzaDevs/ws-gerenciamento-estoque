package br.com.kg.estoque.domain.categoria;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.repository.CustomRepository;

/**
 * Repositório customizado para a entidade {@link Categoria}.
 * Fornece métodos para construir e executar consultas JPQL dinâmicas,
 * especialmente para atender aos requisitos de paginação, busca e ordenação do DataTables.
 */
@Repository
public class CategoriaCustomRepository extends CustomRepository<Categoria>{

    private final Logger logger = Logger.getLogger(CategoriaCustomRepository.class.getName());

    /**
     * Lista as entidades de Categoria para exibição em um DataTables.
     * A consulta é construída dinamicamente para incluir busca em várias colunas (com tratamento de acentos),
     * paginação e ordenação.
     *
     * @param colunas As colunas da tabela que devem ser consideradas na busca.
     * @param params  Os parâmetros da requisição do DataTables (draw, start, length, search, order).
     * @return Uma lista paginada e ordenada de entidades {@link Categoria} que correspondem aos critérios de busca.
     */
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

    /**
     * Calcula o número total de entidades de Categoria que correspondem aos critérios de busca do DataTables.
     * Este método é usado para a paginação, para informar ao DataTables o total de registros filtrados.
     *
     * @param colunas     As colunas da tabela que devem ser consideradas na busca.
     * @param searchValue O valor de busca inserido pelo usuário.
     * @return O número total de registros que correspondem à busca.
     */
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
