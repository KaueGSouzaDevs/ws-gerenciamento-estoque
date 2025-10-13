package br.com.kg.estoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal que inicia a aplicação Spring Boot.
 * <p>
 * A anotação {@link SpringBootApplication} é uma conveniência que adiciona:
 * <ul>
 *   <li>{@code @Configuration}: Marca a classe como uma fonte de definições de beans para o contexto da aplicação.</li>
 *   <li>{@code @EnableAutoConfiguration}: Diz ao Spring Boot para começar a adicionar beans com base nas configurações do classpath, outros beans e várias propriedades.</li>
 *   <li>{@code @ComponentScan}: Diz ao Spring para procurar outros componentes, configurações e serviços no pacote `br.com.kg.estoque`, permitindo que ele encontre os controladores e outros componentes.</li>
 * </ul>
 */
@SpringBootApplication
public class GerenciamentoEstoqueApplication {

	/**
	 * O ponto de entrada principal para a aplicação.
	 *
	 * @param args Argumentos de linha de comando passados durante a inicialização.
	 */
	public static void main(String[] args) {
		SpringApplication.run(GerenciamentoEstoqueApplication.class, args);
	}

}
