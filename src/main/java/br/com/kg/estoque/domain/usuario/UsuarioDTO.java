package br.com.kg.estoque.domain.usuario;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

public class UsuarioDTO {

	@Getter @Setter
	private Long id;

	@Getter @Setter
	private String login;

	@Getter @Setter
	private String nome;

	@Getter @Setter
	private String email;

	@Getter @Setter
	private String senha;

	@Getter @Setter
	private String image64;

	@Getter @Setter
	private String situacaoUsuario;

	@Getter @Setter
	private LocalDateTime dataAtualizacao;

}
