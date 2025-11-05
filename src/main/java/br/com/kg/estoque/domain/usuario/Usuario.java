package br.com.kg.estoque.domain.usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa a entidade Usuário no banco de dados.
 * Esta classe implementa a interface {@link UserDetails} do Spring Security,
 * o que a torna a principal fonte de informações do usuário para fins de autenticação e autorização.
 */
@ToString
@Entity
@Table(name = "usuarios", indexes = {
		@Index(name = "id_usuario_index", columnList = "id_usuario"),
		@Index(name = "login_usuario_index", columnList = "login")
}, uniqueConstraints = {
		@UniqueConstraint(name = "login_unique", columnNames = { "login" }),
		@UniqueConstraint(name = "email_unique", columnNames = { "email" }) })
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	/**
     * Construtor padrão.
     */
	public Usuario() {
	}

	/**
     * Construtor que inicializa o usuário com um login.
     * @param login O nome de usuário (login).
     */
	public Usuario(String login) {
		this.login = login;
	}

	/**
     * Identificador único do usuário.
     */
	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;

	/**
     * O nome de usuário (login) usado para autenticação.
     */
	@Getter
	@Setter
	@Column(length = 35)
	private String login;

	/**
     * O nome completo do usuário.
     */
	@Getter
	@Setter
	@Size(min = 3, max = 30)
	@Column(length = 30)
	private String nome;

	/**
     * O endereço de e-mail do usuário. É obrigatório e deve ser único.
     */
	@Getter
	@Setter
	@NotEmpty(message = "* Informe o e-mail do usuário")
	@Size(max = 70, message = "* Limite de 70 caracteres")
	@Column(length = 70)
	@Email(message = "* E-mail inválido")
	private String email;

	/**
     * A senha do usuário, armazenada de forma criptografada.
     */
	@Getter
	@Setter
	private String senha;

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
	@Getter
	@Setter
	@Transient
	private String image64;

	/**
     * A situação atual da conta do usuário (ex: ATIVO, INATIVO, RESETADO).
     */
	@Getter
	@Setter
	@NotNull(message = "Selecione uma opção")
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private SituacaoUsuario situacaoUsuario;

	/**
     * A data e hora da última atualização dos dados do usuário.
     */
	@Getter
	@Setter
	private LocalDateTime dataAtualizacao;

	/**
     * A lista de grupos de acesso aos quais o usuário pertence.
     * O relacionamento é Muitos-para-Muitos e é carregado de forma EAGER.
     */
	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "rel_usuarios_grupos_acessos", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_grupo_acesso"))
	private List<GrupoAcesso> gruposAcessos;

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

	/**
     * Retorna a senha usada para autenticar o usuário.
     * @return A senha criptografada.
     */
	@Override
	public String getPassword() {
		return this.getSenha();
	}

	/**
     * Retorna o nome de usuário usado para autenticar o usuário.
     * @return O login do usuário.
     */
	@Override
	public String getUsername() {
		return this.getLogin();
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
