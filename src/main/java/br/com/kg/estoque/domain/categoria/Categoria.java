package br.com.kg.estoque.domain.categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a entidade Categoria no banco de dados.
 * Uma categoria é usada para agrupar materiais relacionados.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    /**
     * Identificador único da categoria.
     * Gerado automaticamente pelo banco de dados.
     */
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O nome da categoria.
     * É um campo obrigatório e não pode exceder 20 caracteres.
     */
    @Getter @Setter
    @Column(length = 20)
    @NotBlank(message = "* O nome é obrigatório")
    @Size(max = 20, message = "Máximo de 20 caracteres")
    private String nome;

    /**
     * A situação da categoria (por exemplo, "Ativo" ou "Inativo").
     * É um campo obrigatório.
     */
    @Getter @Setter
    @Column(length = 10)
    @NotBlank(message = "* Obrigatório")
    private String situacao;
    
}
