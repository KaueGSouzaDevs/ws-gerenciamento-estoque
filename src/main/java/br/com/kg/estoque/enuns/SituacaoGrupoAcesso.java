package br.com.kg.estoque.enuns;

public enum SituacaoGrupoAcesso {
    ATIVO("Ativo"),
    DESATIVADO("Desativado");
    
    private String descricao;
    
    private SituacaoGrupoAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
