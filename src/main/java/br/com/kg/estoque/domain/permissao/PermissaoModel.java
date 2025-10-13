package br.com.kg.estoque.domain.permissao;

import org.springframework.security.core.GrantedAuthority;

import br.com.kg.estoque.enuns.Permissao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma permissão (ou "role") no sistema, em conformidade com o Spring Security.
 * <p>
 * Esta entidade é mapeada para a tabela {@code TB_PERMISSAO} e implementa a interface
 * {@link GrantedAuthority}, que é fundamental para o processo de autorização do Spring Security.
 * Cada instância desta classe representa uma única autoridade que pode ser concedida a um usuário.
 * O campo {@code authority} é único para garantir que não existam permissões duplicadas.
 */
@NoArgsConstructor
@Entity
@Table(
		name = "TB_PERMISSAO",
		uniqueConstraints={
				@UniqueConstraint(name="authority_unique", columnNames={"authority"})
				}
		)
public class PermissaoModel implements GrantedAuthority{

	/**
	 * Identificador de versão para serialização.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Identificador único da permissão.
     * Gerado automaticamente pelo banco de dados.
     */
	@Getter @Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long permissaoId;
	
	/**
     * O nome da autoridade/permissão.
     * É armazenado como uma String no banco de dados e é derivado do enum {@link Permissao}.
     * Este campo não pode ser nulo e deve ser único.
     */
	@Setter
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Permissao authority;
	
	/**
     * Retorna o nome da permissão como uma String.
     * Este método satisfaz o contrato da interface {@link GrantedAuthority}.
     *
     * @return A representação em String da autoridade (ex: "ROLE_ADMIN").
     */
	@Override
	public String getAuthority() {
		return authority.toString();
	}

}
