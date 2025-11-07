package br.com.kg.estoque.common;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class FileCloud {
    
    @Getter @Setter
    private String fileName;

    @Getter @Setter
    private String key;

    @Getter @Setter
    private String fileSize;

    @Getter @Setter
    private Long size;

    @Getter @Setter
    private String extension;

    @Getter @Setter
	private String downloadUrl;

	@Getter @Setter
	private Date dataCriacao;

	@Getter @Setter
	private Date dataModificacao;

    @Getter @Setter
    private String caminhoArquivo;

}
