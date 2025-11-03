package br.com.kg.estoque.security;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuração principal para o Spring Security.
 * <p>
 * Habilita a segurança web e a segurança de métodos, definindo as regras de autorização,
 * configuração de login/logout, e o mecanismo de criptografia de senhas.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

	private final ApplicationContext applicationContext;

	/**
     * Constrói a configuração de segurança com o contexto da aplicação.
     *
     * @param applicationContext O contexto da aplicação Spring, usado para obter beans dinamicamente.
     */
	public WebSecurityConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Define a cadeia de filtros de segurança ({@link SecurityFilterChain}) que aplica as regras de segurança às requisições HTTP.
	 * <p>
	 * A configuração inclui:
	 * <ul>
	 *   <li><b>Autorização de Requisições:</b> Permite acesso público a recursos estáticos e à página de login, enquanto exige autenticação para todas as outras requisições. Contém exemplos comentados de como restringir o acesso com base em roles.</li>
	 *   <li><b>CSRF:</b> Desabilita a proteção contra Cross-Site Request Forgery.</li>
	 *   <li><b>Login por Formulário:</b> Configura uma página de login customizada, com manipuladores personalizados para sucesso e falha na autenticação.</li>
	 *   <li><b>Logout:</b> Configura a funcionalidade de logout para invalidar a sessão.</li>
	 *   <li><b>Tratamento de Exceções:</b> Redireciona para uma página de "acesso negado" customizada em caso de falhas de autorização.</li>
	 * </ul>
	 *
	 * @param http O objeto {@link HttpSecurity} para configurar a segurança web.
	 * @return A cadeia de filtros de segurança construída.
	 * @throws Exception se ocorrer um erro durante a configuração.
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/assets/**").permitAll();
			auth.requestMatchers("/login/**").permitAll();
			auth.anyRequest().authenticated();
		});

		http.csrf(AbstractHttpConfigurer::disable);

		http.formLogin(form -> {
			form.loginPage("/login");
			form.successHandler(new CustomAuthenticationSuccessHandler(applicationContext));
			form.failureHandler(new CustomAuthenticationFailureHandler());
			form.permitAll();
		});

		http.logout(logout -> {
			logout.logoutUrl("/logout");
			logout.invalidateHttpSession(true);
		});

		http.exceptionHandling(exception -> exception.accessDeniedPage("/acesso-negado"));

		return http.build();
	}

	/**
	 * Expõe o {@link AuthenticationManager} do Spring Security como um Bean.
	 * Este bean é o principal componente do Spring Security para processar requisições de autenticação.
	 *
	 * @param configuration A configuração de autenticação do Spring.
	 * @return O {@link AuthenticationManager} configurado.
	 * @throws Exception se não for possível obter o AuthenticationManager.
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	/**
	 * Define o bean do {@link PasswordEncoder} que será usado na aplicação.
	 * A implementação escolhida é o {@link BCryptPasswordEncoder}, que é o padrão recomendado
	 * para armazenamento seguro de senhas.
	 *
	 * @return Uma instância de {@link BCryptPasswordEncoder}.
	 */
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
