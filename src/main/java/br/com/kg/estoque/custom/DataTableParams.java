package br.com.kg.estoque.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/**
 * Representa os parâmetros enviados por uma requisição do DataTables.
 * Esta classe armazena as informações de paginação, ordenação e busca
 * necessárias para construir uma consulta dinâmica para a tabela.
 *
 * @author Kauê Gallego de Souza
 */
@ToString
public class DataTableParams {

    /**
     * Construtor padrão.
     */
    public DataTableParams() {
    }

    /**
     * Constrói um objeto DataTableParams com todos os atributos necessários.
     *
     * @param draw        O contador de renderização da tabela.
     * @param start       O índice do registro inicial para paginação.
     * @param length      O número de registros a serem exibidos por página.
     * @param searchValue O valor de busca global.
     * @param orderCol    O índice da coluna a ser ordenada.
     * @param orderDir    A direção da ordenação ("asc" ou "desc").
     */
    public DataTableParams(String draw, Integer start, Integer length, String searchValue, Integer orderCol, String orderDir) {
        this.draw = draw;
        this.start = start;
        this.length = length;
        this.searchValue = searchValue;
        this.orderCol = orderCol;
        this.orderDir = orderDir;
    }

    /**
     * Contador de renderização. Usado pelo DataTables para garantir a sincronia das requisições.
     */
    @Getter @Setter
    private String draw;

    /**
     * Índice do registro inicial para a paginação.
     */
    @Getter @Setter
    private Integer start;
    
    /**
     * Número de registros a serem exibidos por página.
     */
    @Getter @Setter
    private Integer length;

    /**
     * Valor de busca global inserido pelo usuário.
     */
    @Getter @Setter
    private String searchValue;

    /**
     * Índice da coluna pela qual a tabela deve ser ordenada.
     */
    @Getter @Setter
    private Integer orderCol;

    /**
     * Direção da ordenação (ascendente ou descendente).
     */
    @Getter @Setter
    private String orderDir;
}
