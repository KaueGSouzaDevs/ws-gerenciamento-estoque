package br.com.kg.estoque.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // (1) Aplica a configuração para TODAS as rotas da sua API
            .allowedOrigins("http://localhost:5173") // (2) Lista de "casas" (origens) que podem acessar.
                                                                        //     Troque a porta (3000, 8081) pela porta que seu front usa!
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // (3) Quais métodos HTTP são permitidos
            .allowedHeaders("*") // (4) Quais cabeçalhos (headers) são permitidos na requisição
            .allowCredentials(true); // (5) Permite o envio de credenciais (como cookies), se necessário
    }

    /* 
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
    */
}
