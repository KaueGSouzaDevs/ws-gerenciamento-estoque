package br.com.kg.estoque.dev;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe utilitária para gerar senhas criptografadas usando BCrypt.
 * <p>
 * Esta classe é destinada apenas para uso em desenvolvimento, para criar hashes de senha
 * que podem ser inseridos manualmente no banco de dados para testes ou configuração inicial de usuários.
 * Não deve ser utilizada em produção.
 */
public class GeradorCriptografia {

    /**
     * Construtor privado para impedir a instanciação, pois esta é uma classe utilitária.
     */
    private GeradorCriptografia() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Método principal que gera e imprime uma senha criptografada.
     * A senha "123456" é codificada usando {@link BCryptPasswordEncoder} e o hash resultante
     * é impresso no console para uso do desenvolvedor.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GeradorCriptografia.class.getName());
        logger.log(Level.INFO, () -> "Senha criptografada: " + new BCryptPasswordEncoder().encode("123456"));
    }

}
