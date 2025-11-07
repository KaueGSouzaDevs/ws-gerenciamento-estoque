package br.com.kg.estoque.domain.categoria;

import lombok.Getter;
import lombok.Setter;

public class CategoriaDTO {

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String nome;

    @Getter @Setter
    private String situacao;
    
}
