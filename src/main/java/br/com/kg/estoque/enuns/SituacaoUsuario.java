package br.com.kg.estoque.enuns;

import br.com.kg.estoque.custom.EnumComDescricao;

/**
 * Enum que representa os possíveis estados ou situações de uma conta de usuário no sistema.
 * Cada estado tem uma descrição textual amigável associada.
 */
public enum SituacaoUsuario implements EnumComDescricao {
    /**
     * O usuário está ativo e pode usar o sistema normalmente.
     */
    ATIVO("Ativo"),
    /**
     * O usuário está desativado e não pode fazer login.
     */
    DESATIVADO("Desativado"),
    /**
     * A senha do usuário foi resetada e precisa ser alterada no próximo login.
     */
    RESETADO("Resetado"),
    /**
     * O usuário é novo e pode precisar completar etapas adicionais de configuração.
     */
    NOVO("Novo");

    private final String descricao;

    /**
     * Construtor privado para associar uma descrição a cada estado.
     *
     * @param descricao A descrição textual do estado.
     */
    private SituacaoUsuario(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição amigável do estado do usuário.
     *
     * @return a descrição do estado do usuário.
     */
    @Override
    public String getDescricao() {
        return descricao;
    }
}
