package br.com.kg.estoque.dev;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorCriptografia {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    };

}
