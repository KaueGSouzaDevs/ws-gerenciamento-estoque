package br.com.kg.estoque.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

/**
 * Registra a função {@code unaccent} do PostgreSQL no Hibernate.
 * Isso permite o uso da função {@code unaccent} em consultas JPQL e HQL,
 * o que é útil para realizar pesquisas que não diferenciam acentos.
 */
public class UnaccentPsqlFunction implements FunctionContributor  {

    /**
     * Contribui com a função {@code unaccent} para o registro de funções do Hibernate.
     * Este método é chamado pelo Hibernate durante o processo de inicialização para registrar funções customizadas.
     *
     * @param functionContributions O contrato para registrar funções customizadas.
     */
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry().registerNamed(
            "unaccent",
            functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.STRING)
        );
    }
}
