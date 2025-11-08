package br.com.kg.estoque.domain.grupo_acesso;

import java.util.List;

import br.com.kg.estoque.enuns.SituacaoGrupoAcesso;
import lombok.Getter;
import lombok.Setter;

public class GrupoAcessoDTO {

	@Getter @Setter
	private Long id;

	@Getter @Setter
	private String grupo;

	@Getter	@Setter
	private List<String> permissoes;

	@Getter	@Setter
	private String descricao;

	@Getter	@Setter
	private SituacaoGrupoAcesso situacao;
}
