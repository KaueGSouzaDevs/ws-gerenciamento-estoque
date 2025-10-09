package br.com.kg.estoque.repository;

import java.util.List;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Classe abstrata que serve como base para repositórios customizados, fornecendo
 * funcionalidades genéricas para a criação de consultas dinâmicas, especialmente
 * para a integração com o componente jQuery DataTables.
 *
 * @param <T> O tipo da entidade a ser manipulada pelo repositório.
 */
public abstract class CustomRepository<T> {

    /**
     * O EntityManager, injetado pelo container de persistência, usado para interagir
     * com o banco de dados.
     */
    @PersistenceContext
    protected EntityManager em;

    /**
     * Executa uma consulta dinâmica para buscar uma lista paginada, ordenada e filtrada de entidades.
     * Ideal para alimentar um DataTable no lado do servidor.
     *
     * @param colunas As colunas da entidade nas quais a busca deve ser aplicada.
     * @param params Objeto contendo todos os parâmetros da requisição do DataTable (busca, paginação, ordenação).
     * @param type A classe da entidade a ser consultada.
     * @return Uma {@link List} de entidades do tipo {@code T} que correspondem aos critérios.
     */
    public List<T> listEntitiesToDataTable(String[] colunas, DataTableParams params, Class<T> type) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" FROM ").append(type.getSimpleName()).append(" t WHERE t.id > 0");
        jpql.append(" AND ( 1 = 0 ");

        for (String coluna : colunas) {
            if (coluna.equals("id")) {
                // Faz CAST do id direto pra string (sem UNACCENT)
                jpql.append(" OR CAST(t.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent para busca case-insensitive e accent-insensitive
                jpql.append(" OR LOWER(CAST(UNACCENT(t.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }

        jpql.append(" ) ");
        jpql.append(" ORDER BY ").append(colunas[params.getOrderCol()]).append(" ").append(params.getOrderDir());

        var query = em.createQuery(jpql.toString(), type);
        query.setParameter("searchValue", "%" + Auxiliar.removeAcentos(params.getSearchValue().toLowerCase()) + "%");
        query.setFirstResult(params.getStart());
        query.setMaxResults(params.getLength());

        return query.getResultList();
    }

    /**
     * Retorna o número total de entidades que correspondem aos critérios de pesquisa especificados.
     * Este valor é usado pelo DataTable para a propriedade `recordsFiltered`.
     *
     * @param colunas As colunas nas quais a pesquisa será realizada.
     * @param sSearch O termo de pesquisa.
     * @param type O tipo da entidade.
     * @return O número total de entidades que correspondem aos critérios de pesquisa.
     */
    public Long totalEntitiesToDataTable(String[] colunas, String sSearch, Class<T> type) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT COUNT(*) FROM ").append(type.getSimpleName()).append(" t WHERE t.id > 0");
        jpql.append(" AND ( 1 = 0 ");

        for (String coluna : colunas) {
            if (coluna.equals("id")) {
                // Faz CAST do id direto pra string (sem UNACCENT)
                jpql.append(" OR CAST(t.").append(coluna).append(" AS string) LIKE :searchValue ");
            } else {
                // Para os campos String, aplica lower + unaccent para busca case-insensitive e accent-insensitive
                jpql.append(" OR LOWER(CAST(UNACCENT(t.").append(coluna).append(") AS string)) LIKE :searchValue ");
            }
        }

        jpql.append(" ) ");

        var query = em.createQuery(jpql.toString(), Long.class);
        query.setParameter("searchValue", "%" + Auxiliar.removeAcentos(sSearch.toLowerCase()) + "%");

        return query.getSingleResult();
    }
}
