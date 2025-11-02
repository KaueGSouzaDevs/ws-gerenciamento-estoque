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
import br.com.kg.estoque.domain.categoria.Categoria;
import br.com.kg.estoque.domain.categoria.CategoriaService;
import jakarta.validation.Valid;

/**
 * Controlador responsável por gerenciar as requisições relacionadas às categorias.
 * Lida com as operações de CRUD e listagem de dados para a entidade de categoria.
 */
@Controller
@RequestMapping("/categorias")
@PreAuthorize("hasRole('GERENCIAMENTO_CATEGORIAS')")
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Constrói um novo CategoriaController com o CategoriaService especificado.
     *
     * @param categoriaService O serviço para lidar com a lógica de negócios da categoria.
     */
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * Fornece dados para o componente DataTables na página de listagem de categorias.
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
	@GetMapping("/dataTable")
    @ResponseBody
	public DataTableResult jsonDataTable(
        @RequestParam("draw") String draw,
        @RequestParam("start") Integer start,
        @RequestParam("length") Integer length,
        @RequestParam("search[value]") String searchValue,
        @RequestParam("order[0][column]") Integer orderCol,
        @RequestParam("order[0][dir]") String orderDir) {

        DataTableParams params = new DataTableParams(draw, start, length, searchValue, orderCol, orderDir);
		return categoriaService.dataTableCategoria(params);
	}

    /**
     * Exibe a página principal para gerenciamento de categorias.
     *
     * @return Um objeto {@link ModelAndView} para a página de índice de categorias.
     */
    @GetMapping("")
    public ModelAndView index(){
        return new ModelAndView("categorias/index");
    }

    /**
     * Exibe o formulário para criar uma nova categoria.
     *
     * @param categoria Um objeto {@link Categoria} vazio para vincular ao formulário.
     * @return Um objeto {@link ModelAndView} para o formulário modal de criação de categoria.
     */
    @GetMapping("/novo")
    public ModelAndView novo(@ModelAttribute("categoria") Categoria categoria){
        return new ModelAndView("categorias/modal-form");
    }

    /**
     * Exibe o formulário para editar uma categoria existente.
     *
     * @param idCategoria O ID da categoria a ser editada.
     * @return Um objeto {@link ModelAndView} para o formulário modal de edição de categoria, preenchido com os dados da categoria.
     */
    @GetMapping("/{idCategoria}/editar")
    public ModelAndView editar(@PathVariable Long idCategoria){
        ModelAndView model = new ModelAndView("categorias/modal-form");
        Categoria categoria = categoriaService.buscarPorId(idCategoria).orElse(null);
        model.addObject("categoria", categoria);
        return model;
    }

    /**
     * Salva uma nova categoria ou atualiza uma existente.
     *
     * @param categoria O objeto {@link Categoria} a ser salvo, preenchido a partir dos dados do formulário.
     * @param result    O resultado do processo de validação.
     * @return Um {@link ModelAndView} para fechar o modal em caso de sucesso, ou de volta ao formulário em caso de erro de validação.
     */
    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid Categoria categoria, BindingResult result){
        if(result.hasErrors()){
            return novo(categoria);
        }
        categoriaService.salvar(categoria);
        return new ModelAndView("categorias/close-modal");
    }

    /**
     * Exclui uma categoria pelo seu ID.
     *
     * @param idCategoria O ID da categoria a ser excluída.
     * @return Um {@link ResponseEntity} com status 200 (OK) em caso de sucesso ou 404 (Não Encontrado) se a categoria não existir.
     */
    @DeleteMapping("/{idCategoria}/excluir")
    public ResponseEntity<String> excluir(@PathVariable Long idCategoria){
        if(categoriaService.buscarPorId(idCategoria).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        categoriaService.excluir(idCategoria);
        return ResponseEntity.ok("OK");
    }
}
