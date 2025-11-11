package br.com.kg.estoque.dto;

/**
 * DTO (Data Transfer Object) para encapsular os dados da requisição
 * de provisionamento de um novo tenant.
 * <p>
 * Este objeto contém as informações necessárias para criar um novo tenant
 * e o seu primeiro usuário administrador.
 */
public class TenantRequest {

    private String tenantName;
    private String tenantId;
    private String userName;
    private String userEmail;
    private String userPassword;

    // Getters e Setters

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
