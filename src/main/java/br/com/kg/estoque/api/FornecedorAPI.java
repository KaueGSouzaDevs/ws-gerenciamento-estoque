package br.com.kg.estoque.api;

import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.kg.estoque.custom.DataTableRequest;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.domain.fornecedor.Fornecedor;
import br.com.kg.estoque.domain.fornecedor.FornecedorDTO;
import br.com.kg.estoque.domain.fornecedor.FornecedorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/fornecedor")
public class FornecedorAPI {

    private final FornecedorService fornecedorService;
    private final ModelMapper mapper;

    public FornecedorAPI(FornecedorService fornecedorService, ModelMapper mapper) {
        this.fornecedorService = fornecedorService;
        this.mapper = mapper;
    }



    /**
     * Endpoint para listar fornecedores com paginação e filtros via DataTable.
     * @param request Objeto DataTableRequest contendo informações de paginação, ordenação e filtros.
     * @return DataTableResult contendo a lista de fornecedores e metadados para o DataTable.
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTableResult listar(@RequestBody DataTableRequest request) {
        return fornecedorService.dataTableFornecedores(request);
    }



    /**
     * Endpoint para salvar um novo fornecedor.
     * @param request Objeto FornecedorDTO contendo as informações do fornecedor a ser salvo.
     * @return ResponseEntity contendo uma mensagem de sucesso ou erro.
     */
    @PostMapping("/salvar")
    public ResponseEntity<Map<String, String>> salvar(@RequestBody @Valid Fornecedor fornecedor, BindingResult result) {
        /*
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        */

        fornecedorService.salvar(fornecedor);
        return ResponseEntity.ok(Map.of("message", "Fornecedor salvo com sucesso!"));
    }



    /**
     * Endpoint para excluir um fornecedor.
     * @param id ID do fornecedor a ser excluído.
     * @return ResponseEntity contendo uma mensagem de sucesso ou erro.
     */
    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<Map<String, String>> excluir(@PathVariable Long id) {
        try {
            fornecedorService.excluir(id);
            return ResponseEntity.ok(Map.of("message", "Fornecedor excluído com sucesso!"));
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Erro ao excluir fornecedor: " + e.getLocalizedMessage()));
        }
    }



    /**
     * Endpoint para buscar um fornecedor pelo seu ID.
     * @param id ID do fornecedor a ser buscado.
     * @return ResponseEntity contendo o fornecedor encontrado ou uma mensagem de erro.
     */
    @GetMapping("/{id}/editar")
    public ResponseEntity<FornecedorDTO> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Fornecedor> fornecedor = fornecedorService.buscarPorId(id);
            if (fornecedor.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(mapper.map(fornecedor.get(), FornecedorDTO.class));
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

}
