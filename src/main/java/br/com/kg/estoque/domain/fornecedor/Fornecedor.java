package br.com.kg.estoque.domain.fornecedor;

import br.com.kg.estoque.enuns.SituacaoFornecedor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a entidade Fornecedor no banco de dados.
 * Armazena informações sobre os fornecedores de materiais.
 * O CNPJ é definido como uma chave única para evitar duplicidade.
 */
@Entity
@Table(name = "fornecedores", uniqueConstraints = {@UniqueConstraint(columnNames="cnpj_cpf")})
public class Fornecedor {

    /**
     * Identificador único do fornecedor.
     * Gerado automaticamente pelo banco de dados.
     */
    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * CNPJ (Cadastro Nacional da Pessoa Jurídica) do fornecedor.
     * É um campo obrigatório e único.
     */
    @Getter @Setter
    @NotBlank(message = "* O CNPJ ou CPF é obrigatório")
    @Column(name = "cnpj_cpf", length = 18)
    private String cnpjCpf;

    /**
     * Nome ou razão social do fornecedor.
     * É um campo obrigatório e não pode exceder 50 caracteres.
     */
    @Getter @Setter
    @Size(max = 50, message = "Máximo de 50 caracteres")
    @NotBlank(message = "* O nome é obrigatório")
    @Column(name = "nome", length = 50)
    private String nome;

    /**
     * Número de telefone de contato do fornecedor.
     */
    @Getter @Setter
    @Column(length = 15)
    private String telefone;

    /**
     * Endereço de e-mail do fornecedor.
     * Deve ser um e-mail válido e é um campo obrigatório.
     */
    @Getter @Setter
    @Email(message = "E-mail inválido")
    @NotBlank(message = "* O e-mail é obrigatório")
    @Column(name = "email", length = 75)
    private String email;

    /**
     * Nome da pessoa de contato no fornecedor.
     * É um campo obrigatório.
     */
    @Getter @Setter
    @NotBlank(message = "* O contato é obrigatório")
    @Column(name = "contato", length = 50)
    private String contato;

    /**
     * A situação do fornecedor (por exemplo, "Ativo" ou "Inativo").
     * É um campo obrigatório.
     */
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "* A situação é obrigatória")
    @Column(name = "situacao", length = 10)
    private SituacaoFornecedor situacao;
    
}
