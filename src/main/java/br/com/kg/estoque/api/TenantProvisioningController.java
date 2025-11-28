package br.com.kg.estoque.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kg.estoque.domain.tenant.Tenant;
import br.com.kg.estoque.domain.tenant.TenantService;
import br.com.kg.estoque.domain.usuario.Usuario;
import br.com.kg.estoque.domain.usuario.UsuarioService;
import br.com.kg.estoque.dto.TenantRequest;
import br.com.kg.estoque.enuns.SituacaoUsuario;
import br.com.kg.estoque.service.TenantMigrationService;

/**
 * Controller para o provisionamento de novos tenants.
 * <p>
 * Este controller expõe um endpoint para criar um novo tenant, incluindo
 * seu schema de banco de dados, migrações e o primeiro usuário.
 */
@RestController
@RequestMapping("/api/v1/tenants")
public class TenantProvisioningController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TenantService tenantService;

    private final TenantMigrationService tenantMigrationService;
    private final PasswordEncoder passwordEncoder;

    public TenantProvisioningController(
            TenantMigrationService tenantMigrationService,
            PasswordEncoder passwordEncoder) {
        this.tenantMigrationService = tenantMigrationService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo tenant e seu primeiro usuário.
     *
     * @param request O DTO com os dados do tenant e do usuário.
     * @return Uma resposta HTTP com o status da operação.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createTenant(@RequestBody TenantRequest request) throws Exception {

        usuarioService.validaInclusaoTenant(request.getUserEmail());
        try {
            
            // 1. Criar o schema do tenant
            String schemaName = "tenant_" + request.getTenantName();
            tenantMigrationService.migrateTenantSchema(schemaName);
            
            // 2. Salvar o tenant no schema público
            Tenant tenant = new Tenant();
            tenant.setName(request.getTenantName());
            tenant.setSchemaName(schemaName);
            tenantService.salvar(tenant);
            
            // 3. Salvar o primeiro usuário no schema público
            Usuario user = new Usuario();
            user.setName(request.getUserName());
            user.setEmail(request.getUserEmail());
            user.setPassword(passwordEncoder.encode(request.getUserPassword()));
            user.setTenant(tenant);
            user.setSituacaoUsuario(SituacaoUsuario.NOVO);
            user.setDataAtualizacao(LocalDateTime.now());
            usuarioService.save(user);

            return ResponseEntity.ok("Tenant created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create tenant: " + e.getLocalizedMessage());
        }

    }
}
