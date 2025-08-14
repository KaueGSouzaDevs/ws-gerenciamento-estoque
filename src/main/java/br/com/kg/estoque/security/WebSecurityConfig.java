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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

	private ApplicationContext applicationContext;

	public WebSecurityConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}



	/**
	 * Configura a cadeia de filtros de segurança para o aplicativo.
	 * <p>
	 * Define as autorizações de requisição HTTP, permitindo acesso irrestrito
	 * a URLs específicas e exigindo autenticação para outras. Configura CSRF, 
	 * login baseado em formulário, e manipulação de exceções de segurança.
	 * <p>
	 * URLs permitidas sem autenticação:
	 * - "/assets/**" 
	 * - "/login/**"
	 * 
	 * URLs comentadas que requerem permissões de função específicas:
	 * - "/fornecedores**" exige papel "FORNECEDORES"
	 * - "/materiais**" exige papel "MATERIAIS"
	 * - "/categorias**" exige papel "CATEGORIAS"
	 * - "/movimentos**" exige papel "MOVIMENTACAO"
	 * 
	 * O login é configurado para usar uma página personalizada e handlers
	 * de sucesso e falha personalizados.
	 * <p>
	 * Logout é configurado para invalidar a sessão HTTP. Exceções de acesso
	 * negado redirecionam para uma página específica.
	 * 
	 * @param http Configuração de segurança HTTP.
	 * @return A cadeia de filtros de segurança configurada.
	 * @throws Exception Se ocorrer um erro na configuração de segurança.
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> {

			auth.requestMatchers("/assets/**").permitAll();
			auth.requestMatchers("/login/**").permitAll();
			/*/
			auth.requestMatchers("/fornecedores**").hasRole("FORNECEDORES");
			auth.requestMatchers("/materiais**").hasRole("MATERIAIS");
			auth.requestMatchers("/categorias**").hasRole("CATEGORIAS");
			auth.requestMatchers("/movimentos**").hasRole("MOVIMENTACAO");
			auth.requestMatchers("/movimentos**").hasAnyRole("MOVIMENTACAO", "DEL_MOVIMENTACAO");
			auth.requestMatchers(HttpMethod.DELETE, "/movimentos**").hasRole("DEL_MOVIMENTACAO");
			*/

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

		http.exceptionHandling(exception -> exception.accessDeniedPage("/acessoNegado"));

		return http.build();
	}



	/**
	 * Configura o método de autenticação
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}



	@Bean
	/**
	 * Configura o tipo de criptografia utilizado para o banco de dados
	 */
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

}
