package br.com.kg.estoque.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import lombok.Getter;
import lombok.Setter;

/**
 * Componente com escopo de sessão para armazenar parâmetros e preferências do usuário
 * durante sua navegação na aplicação.
 * <p>
 * Sendo um bean com escopo de sessão ({@code WebApplicationContext.SCOPE_SESSION}), uma nova instância
 * desta classe é criada para cada sessão de usuário, garantindo que os dados sejam isolados
 * entre diferentes usuários.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionParameters {

    /**
     * Controla o estado da barra lateral (sidebar).
     * {@code true} se a barra lateral estiver aberta/expandida, {@code false} se estiver fechada/recolhida.
     * O valor padrão é {@code true}.
     */
    @Getter @Setter
    private boolean sidebarAberto = true;

    /**
     * Armazena a preferência de tema do usuário (ex: "dark", "light").
     * Este valor pode ser usado para aplicar um tema visual específico na interface do usuário.
     */
    @Getter @Setter
    private String themeSwitch;

    /**
     * Armazena o ano corrente ou selecionado pelo usuário para ser usado em filtros
     * ou visualizações de dados que dependem do ano.
     */
    @Getter @Setter
    private Integer ano;
}
