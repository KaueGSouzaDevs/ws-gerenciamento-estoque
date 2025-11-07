package br.com.kg.estoque.domain.material;

import java.math.BigDecimal;

import br.com.kg.estoque.domain.categoria.CategoriaDTO;
import br.com.kg.estoque.domain.fornecedor.FornecedorDTO;
import lombok.Getter;
import lombok.Setter;

public class MaterialDTO {

    @Getter @Setter
    private Long id;

    @Getter	@Setter
	private CategoriaDTO categoria;


    @Getter @Setter
    private String nome;

    @Getter @Setter
    private String codigoBarras;

    @Getter @Setter
    private String fabricante;

    @Getter @Setter
    private String unidadeMedida;

    @Getter	@Setter
	private FornecedorDTO fornecedor;

    @Getter @Setter
    private BigDecimal precoCusto;

    @Getter @Setter
	private BigDecimal precoVenda;
    @Getter @Setter
    private String localArmazenagem;

    @Getter @Setter
    private Integer estoqueMaximo;

    @Getter @Setter
    private Integer estoqueMinimo;

    @Getter @Setter
    private Integer saldo;

    @Getter @Setter
    private String situacao;
}
