package br.com.kg.estoque.enuns;

import br.com.kg.estoque.custom.EnumComDescricao;

public enum SituacaoFornecedor implements EnumComDescricao {
    ATIVO("Ativo"),
    INATIVO("Inativo");
    
    private String descricao;
    
    private SituacaoFornecedor(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
