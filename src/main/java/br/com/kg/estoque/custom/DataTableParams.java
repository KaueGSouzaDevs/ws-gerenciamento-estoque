package br.com.kg.estoque.custom;

    /**
     * Retorna uma lista de entidades que correspondem aos critérios de pesquisa especificados.
     * 
     * @param colunas As colunas nas quais a pesquisa será realizada.
     * @param sSearch O termo de pesquisa.
     * @param iSortCol_0 O índice da coluna pela qual os resultados serão ordenados.
     * @param sSortDir_0 A direção da ordenação (ascendente ou descendente).
     * @param iDisplayStart O índice do primeiro resultado a ser retornado.
     * @param iDisplayLength O número máximo de resultados a serem retornados.
     * @param type O tipo de entidade.
     * @return Uma lista de entidades que correspondem aos critérios de pesquisa especificados.
     */

public record DataTableParams(
    Integer iDisplayStart,
    Integer iDisplayLength,
    String sSortDir_0,
    Integer iSortCol_0,
    String sSearch) {
}
