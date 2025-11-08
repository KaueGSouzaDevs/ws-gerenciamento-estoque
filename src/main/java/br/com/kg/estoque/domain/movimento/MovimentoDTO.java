package br.com.kg.estoque.domain.movimento;

import java.time.LocalDateTime;

import br.com.kg.estoque.domain.fornecedor.FornecedorDTO;
import br.com.kg.estoque.domain.material.MaterialDTO;
import br.com.kg.estoque.enuns.TipoMovimento;
import lombok.Getter;
import lombok.Setter;

public class MovimentoDTO {

    @Getter @Setter
    private Long id;

    @Getter @Setter
    private LocalDateTime dataMovimento;

    @Getter @Setter
    private MaterialDTO material;

    @Getter @Setter
    private Integer quantidade;

    @Getter @Setter
    private TipoMovimento tipoMovimento;

    @Getter @Setter
    private String notaFiscal;

    @Getter @Setter
    private FornecedorDTO fornecedor;

    @Getter @Setter
    private String responsavel;
}
