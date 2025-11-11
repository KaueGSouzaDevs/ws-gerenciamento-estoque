package br.com.kg.estoque.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

/**
 * Resolve o identificador do tenant atual para o Hibernate.
 * <p>
 * Esta classe implementa a estratégia do Hibernate para determinar qual tenant
 * (e, consequentemente, qual schema) deve ser usado para a sessão atual.
 * <p>
 * A implementação busca o {@code tenantId} do {@link TenantContext}, que é
 * preenchido por um interceptor no início de cada requisição.
 * Se nenhum tenant for encontrado no contexto, um schema padrão é retornado.
 */
@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_TENANT = "public";

    /**
     * Retorna o identificador do tenant para a sessão atual.
     * <p>
     * O valor retornado é usado pelo Hibernate para selecionar o schema do banco de dados correto.
     *
     * @return O {@code tenantId} do {@link TenantContext} ou o valor padrão {@code "public"}.
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getCurrentTenant();
        return tenantId != null ? tenantId : DEFAULT_TENANT;
    }

    /**
     * Indica se a validação das sessões existentes deve ser feita ao obter uma nova sessão.
     * <p>
     * Retornar {@code true} garante que o Hibernate verifique se as sessões existentes
     * pertencem ao tenant que está sendo resolvido.
     *
     * @return {@code true} para habilitar a validação.
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
