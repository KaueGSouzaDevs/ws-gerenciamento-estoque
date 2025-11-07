package br.com.kg.estoque.enuns;

import br.com.kg.estoque.custom.EnumComDescricao;

public enum SituacaoGrupoAcesso implements EnumComDescricao {
    ATIVO("Ativo"),
    DESATIVADO("Desativado");
    
    private String descricao;
    
    private SituacaoGrupoAcesso(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }
}
