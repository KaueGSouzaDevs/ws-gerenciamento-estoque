package br.com.kg.estoque.domain.usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.kg.estoque.domain.grupo_acesso.GrupoAcesso;
import br.com.kg.estoque.domain.tenant.Tenant;
import br.com.kg.estoque.enuns.SituacaoUsuario;
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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa a entidade Usuário no banco de dados.
 * Esta classe implementa a interface {@link UserDetails} do Spring Security,
 * o que a torna a principal fonte de informações do usuário para fins de autenticação e autorização.
 */
@Entity
@Table(name = "users", schema = "public")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	public Usuario() {
	}

	public Usuario(Long id) {
        this.id = id;
    }



    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
	@Column(name = "id")
	private Long id;

	@Size(min = 3, max = 100)
	@Column(name = "name", length = 100)
    @Getter @Setter
	private String name;

	@NotEmpty(message = "* Informe o e-mail do usuário")
	@Email(message = "* E-mail inválido")
	@Size(max = 100, message = "* Limite de 100 caracteres")
    @Getter @Setter
	@Column(name = "email", length = 100)
	private String email;

	@Getter @Setter
	@Column(name = "password", length = 100)
	private String password;

	public boolean isContaResetada() {
		return this.getSituacaoUsuario() == SituacaoUsuario.RESETADO;
	}

	public boolean isNovaConta() {
		return this.getSituacaoUsuario() == SituacaoUsuario.NOVO;
	}

	@Transient
    @Getter @Setter
	private String image64;

    @NotNull(message = "Selecione uma opção")
	@Enumerated(EnumType.STRING)
	@Getter @Setter
	@Column(name = "situacao_usuario", length = 10)
	private SituacaoUsuario situacaoUsuario;

    @Getter @Setter
	@Column(name = "data_atualizacao")
	private LocalDateTime dataAtualizacao;

    @Getter @Setter
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "rel_usuarios_grupos_acessos", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_grupo_acesso"))
	private List<GrupoAcesso> gruposAcessos;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "id_tenant", foreignKey = @ForeignKey(name = "fk_usuario_tenant"))
    private Tenant tenant;

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
}
