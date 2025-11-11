package br.com.kg.estoque.tenant;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provedor de conexões multi-tenant baseado em schema.
 * <p>
 * Esta classe é responsável por fornecer conexões de banco de dados para o Hibernate
 * e configurar o schema apropriado para o tenant atual.
 * <p>
 * A implementação utiliza um DataSource principal e altera o schema da conexão
 * antes de entregá-la ao Hibernate.
 */
@Component
public class SchemaBasedMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final DataSource dataSource;

    @Autowired
    public SchemaBasedMultiTenantConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Obtém uma conexão para o tenant padrão (público).
     *
     * @return Uma conexão SQL.
     * @throws SQLException se ocorrer um erro ao obter a conexão.
     */
    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Libera uma conexão obtida anteriormente.
     *
     * @param connection A conexão a ser liberada.
     * @throws SQLException se ocorrer um erro ao liberar a conexão.
     */
    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * Obtém uma conexão para um tenant específico e define o schema.
     *
     * @param tenantIdentifier O identificador do tenant (schema).
     * @return Uma conexão SQL com o schema do tenant definido.
     * @throws SQLException se ocorrer um erro ao obter a conexão ou definir o schema.
     */
    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();
        connection.setSchema(tenantIdentifier);
        return connection;
    }

    /**
     * Libera uma conexão de um tenant específico.
     *
     * @param tenantIdentifier O identificador do tenant.
     * @param connection       A conexão a ser liberada.
     * @throws SQLException se ocorrer um erro ao liberar a conexão.
     */
    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.setSchema("public"); // Opcional: resetar para o schema padrão
        releaseAnyConnection(connection);
    }

    /**
     * Indica se a agregação de conexões é suportada.
     *
     * @return {@code false} para desabilitar a agregação.
     */
    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
