package br.com.kg.estoque.tenant;

/**
 * Mantém o identificador do tenant atual para a thread em execução.
 * <p>
 * Esta classe utiliza um {@link ThreadLocal} para armazenar o {@code tenantId}, garantindo
 * que o valor seja isolado por thread e, portanto, seguro em ambientes concorrentes,
 * como em aplicações web.
 * <p>
 * O ciclo de vida do tenantId no contexto deve ser gerenciado externamente,
 * geralmente por um interceptor ou filtro que define o valor no início de
 * uma requisição e o limpa no final.
 */
public class TenantContext {

    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    /**
     * Obtém o identificador do tenant associado à thread atual.
     *
     * @return O {@code tenantId} atual, ou {@code null} se nenhum foi definido.
     */
    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    /**
     * Define o identificador do tenant para a thread atual.
     *
     * @param tenantId O identificador do tenant a ser definido.
     */
    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }

    /**
     * Limpa o identificador do tenant da thread atual.
     * <p>
     * É crucial chamar este método no final de uma requisição para evitar
     * vazamentos de estado entre threads ou requisições.
     */
    public static void clear() {
        currentTenant.remove();
    }
}
