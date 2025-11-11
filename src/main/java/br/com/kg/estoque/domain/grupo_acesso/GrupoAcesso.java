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

/**
 * Representa a entidade Grupo de Acesso no banco de dados.
 * Um grupo de acesso define um conjunto de permissões que podem ser atribuídas a usuários.
 */
@Entity
@Table(name = "grupos_acessos")
public class GrupoAcesso implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * Identificador único do grupo de acesso.
     * Gerado automaticamente pelo banco de dados.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
     * O nome do grupo de acesso (ex: "ADMINISTRADORES", "USUARIOS").
     * É um campo obrigatório.
     */
	@NotEmpty(message = "* Informe o nome do grupo")
	@Size(max = 40, message = "Máximo de 40 caracteres")
	@Column(name="grupo", length=40)
	private String grupo;
	
	/**
     * Lista de permissões (roles) associadas a este grupo.
     * Mapeado para uma tabela separada `permissao_grupo`.
     * As permissões são carregadas de forma EAGER.
     */
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable (name ="permissao_grupo",joinColumns = @JoinColumn (name="id_grupo"), foreignKey = @ForeignKey(name="fk_id_grupo"))
	@Column(name ="permissoes")
	private List<String> permissoes;
	
	/**
     * Descrição textual do propósito do grupo de acesso.
     */
	@Size(max = 100, message = "Máximo de 100 caracteres")
	@Column(name="descricao", length=100)
	private String descricao;
	
	/**
     * A situação do grupo de acesso (ex: "Ativo", "Inativo").
     * É um campo obrigatório.
     */
	@NotNull(message = "* Selecione uma opção")
	@Column(name="situacao", length=10)
	@Enumerated(EnumType.STRING)
	private SituacaoGrupoAcesso situacao;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<String> permissoes) {
        this.permissoes = permissoes;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public SituacaoGrupoAcesso getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoGrupoAcesso situacao) {
        this.situacao = situacao;
    }
}
