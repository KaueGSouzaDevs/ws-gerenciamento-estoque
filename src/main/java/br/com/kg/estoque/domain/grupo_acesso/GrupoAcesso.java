package br.com.kg.estoque.domain.grupo_acesso;

import java.io.Serializable;
import java.util.List;

import br.com.kg.estoque.enuns.SituacaoGrupoAcesso;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa a entidade Grupo de Acesso no banco de dados.
 * Um grupo de acesso define um conjunto de permissões que podem ser atribuídas a usuários.
 */
@Entity
@Table(name = "grupos_acessos")
@ToString
public class GrupoAcesso implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * Identificador único do grupo de acesso.
     * Gerado automaticamente pelo banco de dados.
     */
	@Getter @Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
     * O nome do grupo de acesso (ex: "ADMINISTRADORES", "USUARIOS").
     * É um campo obrigatório.
     */
	@Getter @Setter
	@NotEmpty(message = "* Informe o nome do Grupo")
	@Size(max = 40, message = "Máximo de 40 caracteres")
	@Column(name="grupo", length=40)
	private String grupo;
	
	/**
     * Lista de permissões (roles) associadas a este grupo.
     * Mapeado para uma tabela separada `permissao_grupo`.
     * As permissões são carregadas de forma EAGER.
     */
	@Getter	@Setter
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable (name ="permissao_grupo",joinColumns = @JoinColumn (name="id_grupo"), foreignKey = @ForeignKey(name="fk_id_grupo"))
	@Column(name ="permissoes")
	private List<String> permissoes;
	
	/**
     * Descrição textual do propósito do grupo de acesso.
     */
	@Getter	@Setter
	@Size(max = 100, message = "Máximo de 100 caracteres")
	@Column(name="descricao", length=100)
	private String descricao;
	
	/**
     * A situação do grupo de acesso (ex: "Ativo", "Inativo").
     * É um campo obrigatório.
     */
	@Getter	@Setter
	@NotNull(message = "* Selecione uma opção")
	@Column(name="situacao", length=10)
	@Enumerated(EnumType.STRING)
	private SituacaoGrupoAcesso situacao;
	
}
