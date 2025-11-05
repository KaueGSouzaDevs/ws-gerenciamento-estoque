package br.com.kg.estoque.enuns;

public enum TipoMovimento {
    ENTRADA("Entrada"),
    SAIDA("Saída");
    
    private String descricao;
    
    private TipoMovimento(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
