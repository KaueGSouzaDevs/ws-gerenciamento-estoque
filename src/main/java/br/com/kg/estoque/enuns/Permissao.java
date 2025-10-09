package br.com.kg.estoque.enuns;

/**
 * Enum que representa as permissões básicas no sistema.
 * Estas permissões podem ser atribuídas a grupos de acesso para controlar
 * o que os usuários podem fazer em diferentes partes da aplicação.
 */
public enum Permissao {
    /**
     * Permissão para visualizar ou ler dados.
     */
    READ,
    /**
     * Permissão para criar ou atualizar dados.
     */
    WRITE,
    /**
     * Permissão para excluir dados.
     */
    DELETE;
}
