package br.com.kg.estoque.domain.fornecedor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a entidade Fornecedor no banco de dados.
 * Armazena informações sobre os fornecedores de materiais.
 * O CNPJ é definido como uma chave única para evitar duplicidade.
 */
@Entity
@Table(name = "fornecedores", uniqueConstraints = {@UniqueConstraint(columnNames="cnpj")})
public class Fornecedor {

    /**
     * Identificador único do fornecedor.
     * Gerado automaticamente pelo banco de dados.
     */
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * CNPJ (Cadastro Nacional da Pessoa Jurídica) do fornecedor.
     * É um campo obrigatório e único.
     */
    @Getter @Setter
    @Column(length = 14)
    @NotBlank(message = "* O CNPJ é obrigatório")
    private String cnpj;

    /**
     * Nome ou razão social do fornecedor.
     * É um campo obrigatório e não pode exceder 50 caracteres.
     */
    @Getter @Setter
    @Column(length = 50)
    @NotBlank(message = "* O nome é obrigatório")
    @Size(max = 50, message = "Máximo de 50 caracteres")
    private String nome;

    /**
     * Número de telefone de contato do fornecedor.
     */
    @Getter @Setter
    @Column(length = 14)
    private String telefone;

    /**
     * Endereço de e-mail do fornecedor.
     * Deve ser um e-mail válido e é um campo obrigatório.
     */
    @Getter @Setter
    @Column(length = 50)
    @NotBlank(message = "* O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    /**
     * Nome da pessoa de contato no fornecedor.
     * É um campo obrigatório.
     */
    @Getter @Setter
    @Column(length = 30)
    @NotBlank(message = "* O contato é obrigatório")
    private String contato;

    /**
     * A situação do fornecedor (por exemplo, "Ativo" ou "Inativo").
     * É um campo obrigatório.
     */
    @Getter @Setter
    @Column(length = 10)
    @NotBlank(message = "* A situação é obrigatória")
    private String situacao;
    
}
