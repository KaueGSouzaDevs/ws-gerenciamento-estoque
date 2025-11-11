package br.com.kg.estoque.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor para extrair o identificador do tenant de requisições HTTP
 * e configurar o {@link TenantContext}.
 * <p>
 * Este interceptor é executado antes de cada requisição e tenta resolver
 * o tenant a partir do cabeçalho {@code X-Tenant-ID}.
 * <p>
 * O contexto do tenant é limpo no final da requisição no método {@code afterCompletion}
 * para garantir que o estado não seja mantido entre requisições.
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-ID";

    /**
     * Executado antes do handler do controller.
     * <p>
     * Extrai o {@code tenantId} do cabeçalho da requisição e o define no
     * {@link TenantContext}.
     *
     * @param request  O objeto da requisição HTTP.
     * @param response O objeto da resposta HTTP.
     * @param handler  O handler (controller) escolhido para processar a requisição.
     * @return {@code true} para continuar a execução da cadeia de interceptores.
     * @throws Exception se ocorrer um erro.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getHeader(TENANT_HEADER);
        if (tenantId != null) {
            TenantContext.setCurrentTenant(tenantId);
        }
        return true;
    }

    /**
     * Executado após o handler do controller, mas antes da renderização da view.
     *
     * @param request      O objeto da requisição HTTP.
     * @param response     O objeto da resposta HTTP.
     * @param handler      O handler (controller) que processou a requisição.
     * @param modelAndView O objeto ModelAndView retornado pelo handler.
     * @throws Exception se ocorrer um erro.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Nenhuma ação necessária.
    }

    /**
     * Executado no final da requisição, após a renderização da view.
     * <p>
     * Garante que o contexto do tenant seja limpo para evitar vazamentos
     * de estado entre requisições.
     *
     * @param request  O objeto da requisição HTTP.
     * @param response O objeto da resposta HTTP.
     * @param handler  O handler (controller) que processou a requisição.
     * @param ex       Qualquer exceção lançada durante o processamento.
     * @throws Exception se ocorrer um erro.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TenantContext.clear();
    }
}
