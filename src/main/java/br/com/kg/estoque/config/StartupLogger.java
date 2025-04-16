package br.com.kg.estoque.config; // ou outro subpacote

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {

    private static final Logger logger = LoggerFactory.getLogger(StartupLogger.class);

    @EventListener(ApplicationReadyEvent.class)
    public void logAfterStartup() {
        logger.info("\n\n===================================\n" +
                    "🚀 Aplicação iniciada com sucesso!\n" +
                    "===================================\n");
    }
}
