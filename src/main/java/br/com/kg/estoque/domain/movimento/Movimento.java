package br.com.kg.estoque.domain.movimento;

import java.time.LocalDate;

import br.com.kg.estoque.domain.fornecedor.Fornecedor;
import br.com.kg.estoque.domain.material.Material;
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
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a entidade Movimento no banco de dados.
 * Registra todas as entradas e saídas de materiais do estoque.
 */
@Entity
@Table(name = "movimentos")
public class Movimento {

    /**
     * Identificador único do movimento.
     * Gerado automaticamente pelo banco de dados.
     */
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A data em que o movimento ocorreu.
     * É um campo obrigatório e o valor padrão é a data atual.
     */
    @Getter @Setter
    @NotNull(message = "* Data é obrigatória")
    @Column(nullable = false)
    private LocalDate data = LocalDate.now();

    /**
     * O material que está sendo movimentado.
     * Relacionamento Muitos-para-Um com a entidade {@link Material}.
     */
    @Getter @Setter
    @NotNull(message = "* Material é obrigatório")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="material_id", foreignKey = @ForeignKey(name="fk_material"))
    private Material material;

    /**
     * A quantidade de material movimentada.
     * É um campo obrigatório.
     */
    @Getter @Setter
    @NotNull(message = "* Quantidade é obrigatória")
    @Column(nullable = false)
    private Integer quantidade;

    /**
     * O tipo de movimento, que pode ser "Entrada" ou "Saída".
     * É um campo obrigatório.
     */
    @Getter @Setter
    @NotBlank(message = "* Tipo de movimento é obrigatório")
    @Column(length = 20)
    private String tipo;

    /**
     * O número da nota fiscal associada ao movimento, se aplicável (principalmente para entradas).
     */
    @Getter @Setter
    @Column(length = 50)
    private String notaFiscal;

    /**
     * O fornecedor do material, aplicável para movimentos de entrada.
     * Relacionamento Muitos-para-Um com a entidade {@link Fornecedor}.
     */
    @Getter @Setter
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="fornecedor_id", foreignKey = @ForeignKey(name="fk_fornecedor"))
    private Fornecedor fornecedor;

    /**
     * O nome do responsável por registrar o movimento.
     * É um campo obrigatório.
     */
    @Getter @Setter
    @NotBlank(message = "* Responsável é obrigatório")
    @Column(length = 50)
    private String responsavel;
}
