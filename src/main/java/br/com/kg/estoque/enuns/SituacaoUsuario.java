package br.com.kg.estoque.enuns;

public enum SituacaoUsuario {
    ATIVO("Ativo"),
    DESATIVADO("Desativado"),
    RESETADO("Resetado"),
    NOVO("Novo");

    private final String descricao;

    private SituacaoUsuario(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descriçäo do estado do usuário.
     *
     * @return a descriçäo do estado do usuário
     */
    public String getDescricao() {
        return descricao;
    }


}
