package br.com.kg.estoque.config; // ou outro subpacote

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Componente responsável por registrar uma mensagem quando a aplicação Spring Boot é iniciada com sucesso.
 * Isso fornece uma confirmação visual clara nos logs de que a aplicação está em execução.
 */
@Component
public class StartupLogger {

    private static final Logger logger = LoggerFactory.getLogger(StartupLogger.class);

    /**
     * Ouve o evento {@link ApplicationReadyEvent} e registra uma mensagem de inicialização.
     * Este método é chamado automaticamente pelo Spring Framework assim que a aplicação
     * está totalmente inicializada e pronta para servir requisições.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logAfterStartup() {
        logger.info("""
                    \n===================================
                    🚀 Aplicação iniciada com sucesso!
                    ===================================
                    """);
    }
}
