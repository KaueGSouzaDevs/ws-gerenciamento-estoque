package br.com.kg.estoque.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * DataSource dinâmico que roteia as conexões para o schema do tenant apropriado.
 * <p>
 * Esta classe estende {@link AbstractRoutingDataSource} e utiliza o
 * {@link TenantContext} para determinar qual schema de banco de dados
 * deve ser usado para a transação atual.
 * <p>
 * A lógica de roteamento é implementada no método {@code determineCurrentLookupKey},
 * que retorna o {@code tenantId} do contexto da thread.
 */
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * Determina a chave de lookup para o DataSource atual.
     * <p>
     * A chave retornada corresponde ao identificador do tenant (schema),
     * que é obtido a partir do {@link TenantContext}.
     *
     * @return O {@code tenantId} atual.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant();
    }
}
