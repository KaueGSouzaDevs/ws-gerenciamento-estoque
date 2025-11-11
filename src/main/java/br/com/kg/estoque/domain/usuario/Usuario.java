package br.com.kg.estoque.domain.usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.kg.estoque.domain.Tenant;
import br.com.kg.estoque.domain.grupo_acesso.GrupoAcesso;
import br.com.kg.estoque.enuns.SituacaoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Representa a entidade Usuário no banco de dados.
 * Esta classe implementa a interface {@link UserDetails} do Spring Security,
 * o que a torna a principal fonte de informações do usuário para fins de autenticação e autorização.
 */
@Entity
@Table(name = "users", schema = "public")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	/**
     * Construtor padrão.
     */
	public Usuario() {
	}

	/**
     * Identificador único do usuário.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
     * O nome completo do usuário.
     */
	@Size(min = 3, max = 30)
	@Column(length = 30)
	private String name;

	/**
     * O endereço de e-mail do usuário. É obrigatório e deve ser único.
     */
	@NotEmpty(message = "* Informe o e-mail do usuário")
	@Size(max = 70, message = "* Limite de 70 caracteres")
	@Column(length = 70)
	@Email(message = "* E-mail inválido")
	private String email;

	/**
     * A senha do usuário, armazenada de forma criptografada.
     */
	private String password;

	/**
     * Verifica se a conta do usuário está com o status "RESETADO".
     * @return {@code true} se a situação for RESETADO, {@code false} caso contrário.
     */
	public boolean isContaResetada() {
		return this.getSituacaoUsuario() == SituacaoUsuario.RESETADO;
	}

	/**
     * Verifica se a conta do usuário é uma nova conta (status "NOVO").
     * @return {@code true} se a situação for NOVO, {@code false} caso contrário.
     */
	public boolean isNovaConta() {
		return this.getSituacaoUsuario() == SituacaoUsuario.NOVO;
	}

	/**
     * Campo transiente para armazenar a representação da imagem do usuário em Base64.
     * Não é persistido no banco de dados.
     */
	@Transient
	private String image64;

	/**
     * A situação atual da conta do usuário (ex: ATIVO, INATIVO, RESETADO).
     */
	@NotNull(message = "Selecione uma opção")
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private SituacaoUsuario situacaoUsuario;

	/**
     * A data e hora da última atualização dos dados do usuário.
     */
	private LocalDateTime dataAtualizacao;

	/**
     * A lista de grupos de acesso aos quais o usuário pertence.
     * O relacionamento é Muitos-para-Muitos e é carregado de forma EAGER.
     */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "rel_usuarios_grupos_acessos", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_grupo_acesso"))
	private List<GrupoAcesso> gruposAcessos;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

	/**
     * Retorna as permissões (autoridades) concedidas ao usuário.
     * Este método agrega todas as permissões de todos os grupos de acesso do usuário.
     * @return Uma coleção de {@link GrantedAuthority}.
     */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		this.gruposAcessos.forEach(grupo ->
			grupo.getPermissoes().forEach(p -> {
				var role = new SimpleGrantedAuthority(p);
				if (!authorities.contains(role))
					authorities.add(role);
			})
		);
		return authorities;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }

    public SituacaoUsuario getSituacaoUsuario() {
        return situacaoUsuario;
    }

    public void setSituacaoUsuario(SituacaoUsuario situacaoUsuario) {
        this.situacaoUsuario = situacaoUsuario;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public List<GrupoAcesso> getGruposAcessos() {
        return gruposAcessos;
    }

    public void setGruposAcessos(List<GrupoAcesso> gruposAcessos) {
        this.gruposAcessos = gruposAcessos;
    }

	/**
     * Retorna o nome de usuário usado para autenticar o usuário.
     * @return O login do usuário.
     */
	@Override
	public String getUsername() {
		return this.getEmail();
	}

	/**
     * Indica se a conta do usuário expirou. Uma conta expirada não pode ser autenticada.
     * @return {@code true} se a conta for válida (não expirada).
     */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
     * Indica se o usuário está bloqueado ou desbloqueado. Um usuário bloqueado não pode ser autenticado.
     * @return {@code true} se a conta não estiver bloqueada.
     */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
     * Indica se as credenciais do usuário (senha) expiraram. Credenciais expiradas impedem a autenticação.
     * @return {@code true} se as credenciais forem válidas (não expiradas).
     */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
     * Indica se o usuário está habilitado ou desabilitado. Um usuário desabilitado não pode ser autenticado.
     * @return {@code true} se o usuário estiver habilitado.
     */
	@Override
	public boolean isEnabled() {
		return true;
	}

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
