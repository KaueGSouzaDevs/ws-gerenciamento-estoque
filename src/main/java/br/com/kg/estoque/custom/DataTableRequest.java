package br.com.kg.estoque.custom;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class DataTableRequest {

    @Getter @Setter
    private Integer draw;

    @Getter @Setter
    private Integer start;

    @Getter @Setter
    private Integer length;

    @Getter @Setter
    private List<Order> order;

    @Getter @Setter
    private List<Column> columns;

    @Getter @Setter
    private Search search;


    @ToString
    public static class Search {
        @Getter @Setter
        private String value;

        @Getter @Setter
        private boolean regex;
    }

    @ToString
    public static class Order {
        @Getter @Setter
        private Integer column;

        @Getter @Setter
        private String dir;

        @Getter @Setter
        private String name;
    }

    @ToString
    public static class Column {
        @Getter @Setter
        private String data;

        @Getter @Setter
        private String name;

        @Getter @Setter
        private boolean searchable;

        @Getter @Setter
        private boolean orderable;

        @Getter @Setter
        private Search search;
    }

}
