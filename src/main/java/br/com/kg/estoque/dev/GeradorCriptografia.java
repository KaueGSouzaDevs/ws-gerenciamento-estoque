package br.com.kg.estoque.dev;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorCriptografia {

    
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GeradorCriptografia.class.getName());
        logger.log(Level.ALL, () -> "Senha criptografada: " + new BCryptPasswordEncoder().encode("123456"));
    }

}
