package br.com.kg.estoque.common;

import lombok.Getter;
import lombok.Setter;

public class MensagemErro {

    public MensagemErro(Integer codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    @Getter @Setter
    private Integer codigo;

    @Getter @Setter
    private String mensagem;
}
