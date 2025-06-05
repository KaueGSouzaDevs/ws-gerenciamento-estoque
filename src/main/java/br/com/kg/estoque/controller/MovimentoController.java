package br.com.kg.estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.domain.fornecedor.FornecedorService;
import br.com.kg.estoque.domain.material.MaterialService;
import br.com.kg.estoque.domain.movimento.Movimento;
import br.com.kg.estoque.domain.movimento.MovimentoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/movimentos")
// @PreAuthorize("hasAuthority('ROLE_MOVIMENTACAO')")
public class MovimentoController {

    @Autowired
    private MovimentoService movimentoService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private FornecedorService fornecedorService;

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
		return movimentoService.dataTableMovimento(params);
	}

    /**
     * !Tela inicial contendo a lista de movimentações, tanto entrada como saída.
     * @return
     */
    @GetMapping("")
    public ModelAndView listarAllMovimentos() {
        ModelAndView model = new ModelAndView("movimentos/index");
        return model; 
    }

    /**
     * !Abre uma tela para realizar um cadastro de movimento.
     * @param movimento
     * @param erro
     * @return
     */
    @GetMapping("/novo")
    public ModelAndView novo(Movimento movimento, Boolean erro) {
        ModelAndView model = new ModelAndView("movimentos/form");
        model.addObject("materiaisList", materialService.buscarTodos());
        model.addObject("fornecedoresList", fornecedorService.buscarTodos());
        model.addObject("erro", erro);
        return model; 
    }
    

    /**
     * !Salva a movimentação utilizando uma validação para entrada.
     * @param movimento
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid @ModelAttribute("movimento") Movimento movimento, BindingResult result, RedirectAttributes attributes) {

        if (movimento.getTipo().equals("Entrada")) {
            movimentoService.validaEntradaMaterial(movimento, result);
        };

        if (movimento.getTipo().equalsIgnoreCase("Saida")) {
            if (movimento.getMaterial() != null && movimento.getQuantidade() != null) {
                if (movimento.getMaterial().getSaldo() != null ) {
                    if (movimento.getMaterial().getSaldo() < movimento.getQuantidade()) {
                        result.rejectValue("quantidade", "movimento-error", "* Quantidade insuficiente em estoque");
                    };
                };
            };
        };

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return novo(movimento, true);
        }

        movimentoService.salvarMovimento(movimento);
        attributes.addFlashAttribute("mensagem", "Movimento salvo com sucesso!");
        return new ModelAndView("redirect:/movimentos/novo");
    }

    /**
     * !Realiza a edição de um movimento utilizando o ID.
     * @param id
     * @param model
     * @return
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
     * !Realiza a exclusão de um movimento utilizando o ID.
     * @param id
     * @return
     */
    @DeleteMapping("/{idMovimento}/excluir")
    public ResponseEntity<?> excluir(@PathVariable Long idMovimento){
        if(movimentoService.buscarPorIdOptional(idMovimento).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        movimentoService.excluir(idMovimento);
        return ResponseEntity.ok("OK");
    }
}
