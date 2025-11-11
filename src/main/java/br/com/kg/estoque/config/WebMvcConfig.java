package br.com.kg.estoque.config;

import br.com.kg.estoque.tenant.TenantInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração do Spring MVC para registrar interceptors personalizados.
 * <p>
 * Esta classe é responsável por adicionar o {@link TenantInterceptor} ao
 * registro de interceptors do Spring, garantindo que ele seja executado
 * para todas as requisições recebidas pela aplicação.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final TenantInterceptor tenantInterceptor;

    /**
     * Construtor que injeta as dependências necessárias.
     *
     * @param tenantInterceptor O interceptor de tenant a ser registrado.
     */
    @Autowired
    public WebMvcConfig(TenantInterceptor tenantInterceptor) {
        this.tenantInterceptor = tenantInterceptor;
    }

    /**
     * Adiciona o {@link TenantInterceptor} ao registro de interceptors.
     * <p>
     * O interceptor será aplicado a todos os endpoints da aplicação (/**).
     *
     * @param registry O registro de interceptors do Spring.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor);
    }
}
