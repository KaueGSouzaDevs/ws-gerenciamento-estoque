package br.com.kg.estoque.domain.fornecedor;

import br.com.kg.estoque.enuns.SituacaoFornecedor;
import lombok.Getter;
import lombok.Setter;

public class FornecedorDTO {

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String cnpjCpf;

    @Getter @Setter
    private String nome;

    @Getter @Setter
    private String telefone;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String contato;

    @Getter @Setter
    private SituacaoFornecedor situacao;
    
}
