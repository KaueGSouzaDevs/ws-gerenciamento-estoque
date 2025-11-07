package br.com.kg.estoque.enuns;

import br.com.kg.estoque.custom.EnumComDescricao;

public enum TipoMovimento implements EnumComDescricao {
    ENTRADA("Entrada"),
    SAIDA("Saída");
    
    private String descricao;
    
    private TipoMovimento(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
