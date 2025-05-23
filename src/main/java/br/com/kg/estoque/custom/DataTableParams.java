package br.com.kg.estoque.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/*
 * Classe DataTableParams
 * 
 * Essa classe é utilizada para armazenar parâmetros de configuração para uma tabela de dados. Ela é projetada para ser flexível e permitir a customização da tabela de acordo com as necessidades do usuário.
 * 
 * Atributos
 * 
 * draw: Esse atributo é utilizado para controlar a renderização da tabela. Ele pode ser utilizado para evitar que a tabela seja renderizada várias vezes desnecessariamente.
 * start: Esse atributo define o índice inicial da lista de dados que deve ser exibida na tabela.
 * length: Esse atributo define o número de registros que devem ser exibidos na tabela.
 * searchValue: Esse atributo é utilizado para armazenar o valor de pesquisa digitado pelo usuário. Ele pode ser utilizado para filtrar os dados da tabela.
 * orderCol: Esse atributo define a coluna que deve ser utilizada para ordenar os dados da tabela.
 * orderDir: Esse atributo define a direção da ordenação (ascendente ou descendente).
 * 
 * @Author Kauê Gallego de Souza
 */
@ToString
public class DataTableParams {

    public DataTableParams() {
    }

    public DataTableParams(String draw, Integer start, Integer length, String searchValue, Integer orderCol, String orderDir) {
        this.draw = draw;
        this.start = start;
        this.length = length;
        this.searchValue = searchValue;
        this.orderCol = orderCol;
        this.orderDir = orderDir;
    }

    @Getter @Setter
    private String draw;

    @Getter @Setter
    private Integer start;
    
    @Getter @Setter
    private Integer length;

    @Getter @Setter
    private String searchValue;

    @Getter @Setter
    private Integer orderCol;

    @Getter @Setter
    private String orderDir;
}
