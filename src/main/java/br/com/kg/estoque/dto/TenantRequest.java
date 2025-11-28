package br.com.kg.estoque.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) para encapsular os dados da requisição
 * de provisionamento de um novo tenant.
 * <p>
 * Este objeto contém as informações necessárias para criar um novo tenant
 * e o seu primeiro usuário administrador.
 */
public class TenantRequest {

    @Getter @Setter
    private String tenantName;

    @Getter @Setter
    private String userName;
    
    @Getter @Setter
    private String userEmail;
    
    @Getter @Setter
    private String userPassword;
}
