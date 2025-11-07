package br.com.kg.estoque.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.kg.estoque.custom.DataTableRequest;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.domain.fornecedor.FornecedorService;
import br.com.kg.estoque.domain.material.MaterialService;
import br.com.kg.estoque.domain.movimento.Movimento;
import br.com.kg.estoque.domain.movimento.MovimentoService;
import br.com.kg.estoque.enuns.TipoMovimento;
import jakarta.validation.Valid;

/**
 * Controlador responsável por gerenciar as requisições relacionadas aos movimentos de estoque (entrada e saída).
 * Lida com o registro, edição, exclusão e listagem de movimentos.
 */
@Controller
@RequestMapping("/movimentos")
@PreAuthorize("hasRole('GERENCIAMENTO_MOVIMENTACOES')")
public class MovimentoController {
    
    private final Logger logger = Logger.getLogger(MovimentoController.class.getName());

    private final MovimentoService movimentoService;
    private final MaterialService materialService;
    private final FornecedorService fornecedorService;

    /**
     * Constrói um novo MovimentoController com os serviços especificados.
     *
     * @param movimentoService  O serviço para lidar com a lógica de negócios de movimento.
     * @param materialService   O serviço para buscar dados de materiais.
     * @param fornecedorService O serviço para buscar dados de fornecedores.
     */
    public MovimentoController(MovimentoService movimentoService, MaterialService materialService, FornecedorService fornecedorService) {
        this.movimentoService = movimentoService;
        this.materialService = materialService;
        this.fornecedorService = fornecedorService;
    }

    /**
     * Fornece dados para o componente DataTables na página de listagem de movimentos.
     *
     * @param dataTableRequest O objeto {@link DataTableRequest} contendo os parâmetros de pesquisa.
     * @return Um {@link DataTableResult} com os dados para a tabela.
     */
	@PostMapping("jsonDataTable")
    @ResponseBody
	public DataTableResult jsonDataTable(@RequestBody DataTableRequest dataTableRequest) {
		return movimentoService.dataTableMovimento(dataTableRequest);
	}

    /**
     * Exibe a página principal que lista todos os movimentos de estoque.
     *
     * @return Um {@link ModelAndView} para a página de índice de movimentos.
     */
    @GetMapping("")
    public ModelAndView listarAllMovimentos() {
        return new ModelAndView("movimentos/index");
    }

    /**
     * Exibe o formulário para registrar um novo movimento de estoque.
     *
     * @param movimento O objeto {@link Movimento} para vincular ao formulário.
     * @param erro      Um booleano que indica se ocorreu um erro na submissão anterior.
     * @return Um {@link ModelAndView} para o formulário de movimento.
     */
    @GetMapping("/novo")
    public ModelAndView novo(Movimento movimento, Boolean erro) {
        ModelAndView model = new ModelAndView("movimentos/form");
        model.addObject("materiaisList", materialService.buscarTodosAtivos());
        model.addObject("fornecedoresList", fornecedorService.buscarTodosAtivos());
        model.addObject("erro", erro);
        return model; 
    }

    /**
     * Salva um novo movimento de estoque após validação.
     * Valida as regras de negócio para movimentos de entrada e saída.
     *
     * @param movimento  O objeto {@link Movimento} preenchido a partir do formulário.
     * @param result     O resultado da validação dos dados.
     * @param attributes Atributos para passar mensagens para a view de redirecionamento.
     * @return Um {@link ModelAndView} redirecionando para o formulário em caso de sucesso, ou de volta ao formulário em caso de erro.
     */
    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid @ModelAttribute("movimento") Movimento movimento, BindingResult result, RedirectAttributes attributes) {

        if (movimento.getTipoMovimento() == TipoMovimento.ENTRADA) {
            movimentoService.validaEntradaMaterial(movimento, result);
        }

        if (movimento.getTipoMovimento() == TipoMovimento.SAIDA && movimento.getMaterial() != null && movimento.getQuantidade() != null && movimento.getMaterial().getSaldo() != null && movimento.getMaterial().getSaldo() < movimento.getQuantidade()) {
                result.rejectValue("quantidade", "movimento-error", "* Quantidade insuficiente em estoque");
        }

        if (result.hasErrors()) {
            logger.log(Level.WARNING , () -> result.getAllErrors().toString());
            return novo(movimento, true);
        }

        movimentoService.salvarMovimento(movimento);
        attributes.addFlashAttribute("mensagem", "Movimento salvo com sucesso!");
        return new ModelAndView("redirect:/movimentos/novo");
    }

    /**
     * Exibe o formulário para editar um movimento existente.
     *
     * @param id    O ID do movimento a ser editado.
     * @param model O modelo para adicionar atributos para a view.
     * @return Um {@link ModelAndView} para o formulário de movimento preenchido, ou redireciona para a lista se não encontrado.
     */
    @GetMapping("/{id}/editar")
    public ModelAndView getMovimentoById(@PathVariable Long id, Model model) {
        Movimento movimento = movimentoService.buscarPorId(id);
        if (movimento != null) {
            model.addAttribute("movimento", movimento);
            model.addAttribute("materiaisList", materialService.buscarTodos());
            model.addAttribute("fornecedoresList", fornecedorService.buscarTodos());
            return new ModelAndView("movimentos/form");
        }
        return new ModelAndView("redirect:/movimentos");
    }

    /**
     * Exclui um movimento de estoque pelo seu ID.
     *
     * @param idMovimento O ID do movimento a ser excluído.
     * @return Um {@link ResponseEntity} com status 200 (OK) em caso de sucesso ou 404 (Não Encontrado) se o movimento não existir.
     */
    @DeleteMapping("/{idMovimento}/excluir")
    public ResponseEntity<String> excluir(@PathVariable Long idMovimento){
        if(movimentoService.buscarPorIdOptional(idMovimento).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        movimentoService.excluir(idMovimento);
        return ResponseEntity.ok("OK");
    }
}
