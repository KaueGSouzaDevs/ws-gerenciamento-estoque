package br.com.kg.estoque.enuns;

import br.com.kg.estoque.custom.EnumComDescricao;

public enum SituacaoCategoria implements EnumComDescricao {
    ATIVO("Ativo"),
    INATIVO("Inativo");
    
    private String descricao;
    
    private SituacaoCategoria(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
