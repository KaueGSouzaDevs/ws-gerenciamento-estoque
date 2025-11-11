package br.com.kg.estoque.service;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Serviço para gerenciar as migrações de banco de dados dos tenants.
 * <p>
 * Este serviço é responsável por executar as migrações do Flyway em um
 * schema de tenant específico de forma programática.
 * <p>
 * É utilizado durante o processo de provisionamento de um novo tenant para
 * garantir que o schema correspondente seja criado e atualizado com a
 * estrutura de tabelas mais recente.
 */
@Service
public class TenantMigrationService {

    private final DataSource dataSource;

    @Autowired
    public TenantMigrationService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Executa as migrações do Flyway para um determinado schema de tenant.
     *
     * @param schema O nome do schema do tenant a ser migrado.
     */
    public void migrateTenantSchema(String schema) {
        Flyway flyway = Flyway.configure()
                .locations("classpath:db/migration/tenant")
                .dataSource(dataSource)
                .schemas(schema)
                .load();
        flyway.migrate();
    }
}
