package br.com.kg.estoque.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import br.com.kg.estoque.domain.categoria.CategoriaService;
import br.com.kg.estoque.domain.fornecedor.FornecedorService;
import br.com.kg.estoque.domain.material.Material;
import br.com.kg.estoque.domain.material.MaterialService;
import br.com.kg.estoque.enuns.UnidadeMedida;
import jakarta.validation.Valid;

/**
 * Controlador responsável por gerenciar as requisições relacionadas aos materiais.
 * Lida com as operações de CRUD e listagem de dados para a entidade de material.
 */
@Controller
@RequestMapping("/materiais")
@PreAuthorize("hasRole('GERENCIAMENTO_MATERIAIS')")
public class MaterialController {
    
    private final MaterialService materialService;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;

    /**
     * Constrói um novo MaterialController com os serviços especificados.
     *
     * @param materialService   O serviço para lidar com a lógica de negócios de material.
     * @param categoriaService  O serviço para buscar dados de categorias.
     * @param fornecedorService O serviço para buscar dados de fornecedores.
     */
    public MaterialController(MaterialService materialService, CategoriaService categoriaService, FornecedorService fornecedorService) {
        this.materialService = materialService;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
    }

    /**
     * Fornece dados para o componente DataTables na página de listagem de materiais.
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
		return materialService.dataTableMaterial(params);
	}

    /**
     * Exibe a página principal para gerenciamento de materiais.
     *
     * @return Um objeto {@link ModelAndView} para a página de índice de materiais.
     */
    @GetMapping("")
    public ModelAndView index(){
        return new ModelAndView("materiais/index");
    }

    /**
     * Exibe o formulário para criar um novo material.
     * Popula o modelo com listas de unidades de medida, categorias e fornecedores.
     *
     * @param material Um objeto {@link Material} vazio para vincular ao formulário.
     * @return Um objeto {@link ModelAndView} para o formulário modal de criação de material.
     */
    @GetMapping("/novo")
    public ModelAndView novo(@ModelAttribute("material") Material material){
        ModelAndView model = new ModelAndView("materiais/modal-form");
        model.addObject("unidadeMedidaList", UnidadeMedida.values());
        model.addObject("categoriaList", categoriaService.buscarTodos());
        model.addObject("fornecedorList", fornecedorService.buscarTodos());
        return model;
    }

    /**
     * Exibe o formulário para editar um material existente.
     * Popula o modelo com os dados do material e listas de unidades de medida, categorias e fornecedores.
     *
     * @param idMaterial O ID do material a ser editado.
     * @return Um objeto {@link ModelAndView} para o formulário modal de edição de material.
     */
    @GetMapping("/{idMaterial}/editar")
    public ModelAndView editar(@PathVariable Long idMaterial){
        ModelAndView model = new ModelAndView("materiais/modal-form");
        model.addObject("material", materialService.buscarPorId(idMaterial).orElse(null));
        model.addObject("unidadeMedidaList", UnidadeMedida.values());
        model.addObject("categoriaList", categoriaService.buscarTodos());
        model.addObject("fornecedorList", fornecedorService.buscarTodos());
        return model;
    }

    /**
     * Salva um novo material ou atualiza um existente.
     *
     * @param material O objeto {@link Material} a ser salvo, preenchido a partir dos dados do formulário.
     * @param result   O resultado do processo de validação.
     * @return Um {@link ModelAndView} para o formulário se houver erros, ou para fechar o modal em caso de sucesso.
     */
    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid Material material, BindingResult result){

        System.out.println(result.getErrorCount());

        if(result.hasErrors()){
            return novo(material);
        }
        materialService.salvar(material);
        return new ModelAndView("materiais/close-modal");
    }

    /**
     * Exclui um material pelo seu ID.
     *
     * @param idMaterial O ID do material a ser excluído.
     * @return Um {@link ResponseEntity} com status 200 (OK) em caso de sucesso ou 404 (Não Encontrado) se o material não existir.
     */
    @DeleteMapping("/{idMaterial}/excluir")
    public ResponseEntity<String> excluir(@PathVariable Long idMaterial){
        if(materialService.buscarPorId(idMaterial).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        materialService.excluir(idMaterial);
        return ResponseEntity.ok("OK");
    }

}
