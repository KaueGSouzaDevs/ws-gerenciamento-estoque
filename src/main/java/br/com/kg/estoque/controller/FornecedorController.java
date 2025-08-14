package br.com.kg.estoque.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.domain.fornecedor.Fornecedor;
import br.com.kg.estoque.domain.fornecedor.FornecedorService;
import jakarta.validation.Valid;

/**
 * Controlador responsável por gerenciar as requisições relacionadas aos fornecedores.
 */
@Controller
@RequestMapping("/fornecedores")
// @PreAuthorize("hasAuthority('ROLE_FORNECEDORES')")
public class FornecedorController {

    private FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }



    /**
     * Gera json dinâmico para Data Table (CRUD)
     */
    @GetMapping("jsonDataTable")
    @ResponseBody
    public DataTableResult jsonDataTable(
            @RequestParam("draw") String draw,
            @RequestParam("start") Integer start,
            @RequestParam("length") Integer length,
            @RequestParam("search[value]") String searchValue,
            @RequestParam("order[0][column]") Integer orderCol,
            @RequestParam("order[0][dir]") String orderDir) {

        DataTableParams params = new DataTableParams(draw, start, length, searchValue, orderCol, orderDir);
        return fornecedorService.dataTableFornecedores(params);
    }



    /**
     * Retorna a página inicial dos fornecedores, exibindo a lista de todos os fornecedores cadastrados.
     * @return O ModelAndView contendo a página inicial dos fornecedores.
     */
    @GetMapping("")
    public ModelAndView index(){
        return new ModelAndView("fornecedores/index");
    }



    /**
     * Retorna a página de cadastro de um novo fornecedor.
     * @param fornecedor O objeto Fornecedor a ser preenchido no formulário.
     * @return O ModelAndView contendo a página de cadastro de fornecedor.
     */
    @GetMapping("/novo")
    public ModelAndView novo(@ModelAttribute("fornecedor") Fornecedor fornecedor){
        return new ModelAndView("fornecedores/form");
    }



    /**
     * Salva um fornecedor no banco de dados.
     * @param fornecedor O objeto Fornecedor a ser salvo.
     * @param result O objeto BindingResult contendo os resultados da validação do formulário.
     * @return O ModelAndView redirecionando para a página inicial dos fornecedores em caso de sucesso, ou para a página de cadastro em caso de erro.
     */
    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid Fornecedor fornecedor, BindingResult result){
        fornecedorService.validaInclusao(fornecedor, result);
        fornecedorService.validaAlteracao(fornecedor, result);
        
        if(result.hasErrors()){
            return new ModelAndView("fornecedores/form");
        }        
        fornecedorService.salvar(fornecedor);
        return new ModelAndView("redirect:/fornecedores");
    }



    /**
     * Retorna a página de edição de um fornecedor existente.
     * @param idFornecedor O ID do fornecedor a ser editado.
     * @return O ModelAndView contendo a página de edição de fornecedor, ou a página de erro 404 caso o fornecedor não seja encontrado.
     */
    @GetMapping("{idFornecedor}/editar")
    public ModelAndView editar(@PathVariable Long idFornecedor){
        Optional<Fornecedor> fornecedor = fornecedorService.buscarPorId(idFornecedor);
        if(fornecedor.isEmpty()){
            return new ModelAndView("fornecedores/erro404");
        }
        ModelAndView model = new ModelAndView("fornecedores/form");
        model.addObject("fornecedor", fornecedor.get());
        return model;
    }



    /**
     * Exclui um fornecedor do banco de dados.
     * @param idFornecedor O ID do fornecedor a ser excluído.
     * @return A ResponseEntity com o status OK em caso de sucesso, ou o status Not Found caso o fornecedor não seja encontrado.
     */
    @DeleteMapping("/{idFornecedor}/excluir")
    public ResponseEntity<String> excluir(@PathVariable Long idFornecedor){
        if(fornecedorService.buscarPorId(idFornecedor).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        fornecedorService.excluir(idFornecedor);
        return ResponseEntity.ok("OK");
    }
}
