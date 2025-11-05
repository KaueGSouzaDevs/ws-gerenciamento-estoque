package br.com.kg.estoque.domain.material;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

import br.com.kg.estoque.domain.categoria.Categoria;
import br.com.kg.estoque.domain.fornecedor.Fornecedor;
import br.com.kg.estoque.enuns.SituacaoMaterial;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a entidade Material no banco de dados.
 * Armazena informações detalhadas sobre cada item do estoque.
 */
@Entity
@Table(name = "materiais")
public class Material {

    /**
     * Identificador único do material.
     * Gerado automaticamente pelo banco de dados.
     */
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * A categoria à qual o material pertence.
     * Relacionamento Muitos-para-Um com a entidade {@link Categoria}.
     */
    @Getter	@Setter
	@NotNull(message = "* Categoria obrigatória")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_categoria", foreignKey = @ForeignKey(name="fk_categoria"))
	private Categoria categoria;

    /**
     * O nome ou descrição do material.
     * É um campo obrigatório com no máximo 100 caracteres.
     */
    @Getter @Setter
    @NotBlank(message = "* Nome é obrigatório")
    @Size(max = 100, message = "Máximo de 100 caracteres")
    @Column(name = "nome", length = 100)
    private String nome;

    /**
     * O código de barras do material, se aplicável.
     */
    @Getter @Setter
    @Column(name = "codigo_barras", length = 13)
    private String codigoBarras;

    /**
     * O nome do fabricante do material.
     */
    @Getter @Setter
    @Column(name = "fabricante", length = 30)
    private String fabricante;

    /**
     * A unidade de medida do material (ex: "Peça", "Caixa", "Metro").
     * É um campo obrigatório.
     */
    @Getter @Setter
    @NotBlank(message = "* U.M obrigatória")
    @Column(name = "unidade_medida", length = 20)
    private String unidadeMedida;

    /**
     * O fornecedor principal do material.
     * Relacionamento Muitos-para-Um com a entidade {@link Fornecedor}.
     */
    @Getter	@Setter
    @NotNull(message="* Fornecedor obrigatório")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_fornecedor", foreignKey = @ForeignKey(name="fk_fornecedor"))
	private Fornecedor fornecedor;

    /**
     * O preço de custo monetário do material.
     * Armazenado com precisão de 11 dígitos, sendo 2 para casas decimais.
     */
    @Getter @Setter
    @NumberFormat(pattern = "#,###,###,###.##")
    @NotNull(message="* Preço de custo é obrigatório")
    @Column(name = "preco_custo", precision = 11, scale = 2, columnDefinition = "Decimal(11,2)")
    private BigDecimal precoCusto;

    /**
     * O preço de venda monetário do material.
     * Armazenado com precisão de 11 dígitos, sendo 2 para casas decimais.
     */
    @Getter @Setter
	@NotNull(message="* Preço de venda é obrigatório")
	@NumberFormat(pattern = "#,###,###,###.##")
	@Column(name = "preco_venda", precision = 11, scale = 2, columnDefinition = "Decimal(11,2)")
	private BigDecimal precoVenda;

    /**
     * A localização física do material no estoque.
     * É um campo obrigatório.
     */
    @Getter @Setter
    @NotBlank(message = "* Local obrigatório")
    @Column(name = "local_armazenagem", length = 30)
    private String localArmazenagem;
    
    /**
     * A quantidade máxima deste material que deve ser mantida em estoque.
     */
    @Getter @Setter
    @NotNull(message = "* Qtd obrigatória")
    @Column(name = "estoque_maximo")
    private Integer estoqueMaximo;

    /**
     * A quantidade mínima deste material que deve ser mantida em estoque.
     * Serve como ponto de reposição.
     */
    @Getter @Setter
    @NotNull(message = "* Qtd obrigatória")
    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo;

    /**
     * O saldo atual do material em estoque.
     * Inicializado com 0 por padrão.
     */
    @Getter @Setter
    @NotNull(message = "* Saldo obrigatório")
    @Column(name = "saldo", nullable = false, columnDefinition = "int default 0")
    private Integer saldo = 0;

    /**
     * A situação do material (ex: "Ativo", "Inativo").
     * É um campo obrigatório.
     */
    @Getter @Setter
    @NotNull(message = "* Situação obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", length = 10)
    private SituacaoMaterial situacao;
}
