package br.com.kg.estoque.controller;

import br.com.kg.estoque.domain.Tenant;
import br.com.kg.estoque.domain.usuario.Usuario;
import br.com.kg.estoque.dto.TenantRequest;
import br.com.kg.estoque.repository.TenantRepository;
import br.com.kg.estoque.repository.UsuarioRepository;
import br.com.kg.estoque.service.TenantMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para o provisionamento de novos tenants.
 * <p>
 * Este controller expõe um endpoint para criar um novo tenant, incluindo
 * seu schema de banco de dados, migrações e o primeiro usuário.
 */
@RestController
@RequestMapping("/api/tenants")
public class TenantProvisioningController {

    private final TenantRepository tenantRepository;
    private final UsuarioRepository usuarioRepository;
    private final TenantMigrationService tenantMigrationService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TenantProvisioningController(
            TenantRepository tenantRepository,
            UsuarioRepository usuarioRepository,
            TenantMigrationService tenantMigrationService,
            PasswordEncoder passwordEncoder) {
        this.tenantRepository = tenantRepository;
        this.usuarioRepository = usuarioRepository;
        this.tenantMigrationService = tenantMigrationService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo tenant e seu primeiro usuário.
     *
     * @param request O DTO com os dados do tenant e do usuário.
     * @return Uma resposta HTTP com o status da operação.
     */
    @PostMapping
    public ResponseEntity<String> createTenant(@RequestBody TenantRequest request) {
        // 1. Criar o schema do tenant
        String schemaName = "tenant_" + request.getTenantId();
        tenantMigrationService.migrateTenantSchema(schemaName);

        // 2. Salvar o tenant no schema público
        Tenant tenant = new Tenant();
        tenant.setTenantId(request.getTenantId());
        tenant.setName(request.getTenantName());
        tenant.setSchemaName(schemaName);
        tenantRepository.save(tenant);

        // 3. Salvar o primeiro usuário no schema público
        Usuario user = new Usuario();
        user.setName(request.getUserName());
        user.setEmail(request.getUserEmail());
        user.setPassword(passwordEncoder.encode(request.getUserPassword()));
        user.setTenant(tenant);
        usuarioRepository.save(user);

        return ResponseEntity.ok("Tenant created successfully");
    }
}
