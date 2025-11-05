package br.com.kg.estoque.enuns;

public enum SituacaoCategoria {
    ATIVO("Ativo"),
    INATIVO("Inativo");
    
    private String descricao;
    
    private SituacaoCategoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
