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
 * Lida com as operações de CRUD e listagem de dados para a entidade de fornecedor.
 */
@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    /**
     * Constrói um novo FornecedorController com o FornecedorService especificado.
     *
     * @param fornecedorService O serviço para lidar com a lógica de negócios do fornecedor.
     */
    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    /**
     * Fornece dados para o componente DataTables na página de listagem de fornecedores.
     * Este endpoint é chamado via AJAX pela biblioteca DataTables para popular a tabela.
     *
     * @param draw        O contador de sorteio ao qual esta solicitação está respondendo.
     * @param start       O índice do registro inicial (para paginação).
     * @param length      O número de registros a serem exibidos (para paginação).
     * @param searchValue O valor de pesquisa global.
     * @param orderCol    O índice da coluna a ser ordenada.
     * @param orderDir    A direção da ordenação (asc ou desc).
     * @return Um objeto {@link DataTableResult} contendo os dados para a tabela.
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
     * Exibe a página principal para gerenciamento de fornecedores.
     *
     * @return Um objeto {@link ModelAndView} para a página de índice de fornecedores.
     */
    @GetMapping("")
    public ModelAndView index(){
        return new ModelAndView("fornecedores/index");
    }

    /**
     * Exibe o formulário para criar um novo fornecedor.
     *
     * @param fornecedor Um objeto {@link Fornecedor} vazio para vincular ao formulário.
     * @return Um objeto {@link ModelAndView} para o formulário de criação de fornecedor.
     */
    @GetMapping("/novo")
    public ModelAndView novo(@ModelAttribute("fornecedor") Fornecedor fornecedor){
        return new ModelAndView("fornecedores/form");
    }

    /**
     * Salva um novo fornecedor ou atualiza um existente.
     * Realiza a validação antes de salvar.
     *
     * @param fornecedor O objeto {@link Fornecedor} a ser salvo, preenchido a partir dos dados do formulário.
     * @param result     O resultado do processo de validação.
     * @return Um {@link ModelAndView} redirecionando para a lista de fornecedores em caso de sucesso, ou de volta ao formulário em caso de erro de validação.
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
     * Exibe o formulário para editar um fornecedor existente.
     *
     * @param idFornecedor O ID do fornecedor a ser editado.
     * @return Um {@link ModelAndView} para o formulário de edição de fornecedor, preenchido com os dados do fornecedor, ou uma página de erro 404 se não for encontrado.
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
     * Exclui um fornecedor pelo seu ID.
     *
     * @param idFornecedor O ID do fornecedor a ser excluído.
     * @return Um {@link ResponseEntity} com status 200 (OK) em caso de sucesso ou 404 (Não Encontrado) se o fornecedor não existir.
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
