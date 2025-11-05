package br.com.kg.estoque.enuns;

public enum SituacaoFornecedor {
    ATIVO("Ativo"),
    INATIVO("Inativo");
    
    private String descricao;
    
    private SituacaoFornecedor(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
