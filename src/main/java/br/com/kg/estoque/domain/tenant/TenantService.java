package br.com.kg.estoque.domain.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {
    
    @Autowired
    private TenantRepository tenantRepository;

    public void salvar(Tenant tenant) {
        tenantRepository.save(tenant);
    }


}
