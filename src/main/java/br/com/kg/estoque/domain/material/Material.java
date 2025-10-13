package br.com.kg.estoque.domain.material;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

import br.com.kg.estoque.domain.categoria.Categoria;
import br.com.kg.estoque.domain.fornecedor.Fornecedor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Long id;

    /**
     * A categoria à qual o material pertence.
     * Relacionamento Muitos-para-Um com a entidade {@link Categoria}.
     */
    @Getter	@Setter
	@NotNull(message = "* Categoria obrigatória")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="categoria_id", foreignKey = @ForeignKey(name="fk_categoria"))
	private Categoria categoria;

    /**
     * O nome ou descrição do material.
     * É um campo obrigatório com no máximo 100 caracteres.
     */
    @Getter @Setter
    @Column(length = 100)
    @NotBlank(message = "* Nome é obrigatório")
    @Size(max = 100, message = "Máximo de 100 caracteres")
    private String nome;

    /**
     * O código de barras do material, se aplicável.
     */
    @Getter @Setter
    @Column(length = 13)
    private String codigoBarras;

    /**
     * O nome do fabricante do material.
     */
    @Getter @Setter
    @Column(length = 30)
    private String fabricante;

    /**
     * A unidade de medida do material (ex: "Peça", "Caixa", "Metro").
     * É um campo obrigatório.
     */
    @Getter @Setter
    @Column(length = 20)
    @NotBlank(message = "* UM obrigatória")
    private String unidadeMedida;

    /**
     * O fornecedor principal do material.
     * Relacionamento Muitos-para-Um com a entidade {@link Fornecedor}.
     */
    @Getter	@Setter
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fornecedor_id", foreignKey = @ForeignKey(name="fk_fornecedor"))
    @NotNull(message="* Fornecedor obrigatório")
	private Fornecedor fornecedor;

    /**
     * O valor monetário do material.
     * Armazenado com precisão de 11 dígitos, sendo 2 para casas decimais.
     */
    @Getter @Setter
	@NotNull(message="* Valor obrigatório")
	@NumberFormat(pattern = "#,###,###,###.##")
	@Column(precision = 11, scale = 2, columnDefinition = "Decimal(11,2)")
	private BigDecimal valor;

    /**
     * A localização física do material no estoque.
     * É um campo obrigatório.
     */
    @Getter @Setter
    @Column(length = 30)
    @NotBlank(message = "* Local obrigatório")
    private String localArmazenagem;
    
    /**
     * A quantidade máxima deste material que deve ser mantida em estoque.
     */
    @Getter @Setter
    @NotNull(message = "* Qtd obrigatória")
    private Integer estoqueMaximo;

    /**
     * A quantidade mínima deste material que deve ser mantida em estoque.
     * Serve como ponto de reposição.
     */
    @Getter @Setter
    @NotNull(message = "* Qtd obrigatória")
    private Integer estoqueMinimo;

    /**
     * O saldo atual do material em estoque.
     * Inicializado com 0 por padrão.
     */
    @Getter @Setter
    @NotNull(message = "* Saldo obrigatório")
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer saldo = 0;

    /**
     * A situação do material (ex: "Ativo", "Inativo").
     * É um campo obrigatório.
     */
    @Getter @Setter
    @Column(length = 10)
    @NotBlank(message = "* Situação obrigatória")
    private String situacao;
}
