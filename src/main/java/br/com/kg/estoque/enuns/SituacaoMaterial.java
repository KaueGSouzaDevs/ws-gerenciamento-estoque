package br.com.kg.estoque.enuns;

import br.com.kg.estoque.custom.EnumComDescricao;

public enum SituacaoMaterial implements EnumComDescricao {
    ATIVO("Ativo"),
    INATIVO("Inativo");
    
    private String descricao;
    
    private SituacaoMaterial(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
