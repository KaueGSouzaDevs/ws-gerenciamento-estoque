package br.com.kg.estoque.controller;

import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.domain.grupo_acesso.GrupoAcesso;
import br.com.kg.estoque.domain.grupo_acesso.GrupoAcessoService;
import br.com.kg.estoque.domain.usuario.Usuario;
import br.com.kg.estoque.domain.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controlador responsável por gerenciar as requisições relacionadas aos usuários.
 * Lida com as operações de CRUD e listagem de dados para a entidade de usuário.
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final GrupoAcessoService grupoAcessoService;

    /**
     * Constrói um novo UsuarioController com os serviços especificados.
     *
     * @param usuarioService     O serviço para lidar com a lógica de negócios do usuário.
     * @param grupoAcessoService O serviço para lidar com a lógica de negócios do grupo de acesso.
     */
    public UsuarioController(UsuarioService usuarioService, GrupoAcessoService grupoAcessoService) {
        this.usuarioService = usuarioService;
        this.grupoAcessoService = grupoAcessoService;
    }

    /**
     * Fornece a lista de todos os grupos de acesso para os modelos.
     *
     * @return Uma lista de {@link GrupoAcesso}.
     */
    @ModelAttribute("gruposAcessos")
    public List<GrupoAcesso> allGruposAcessos() {
        return grupoAcessoService.findAll();
    }

    /**
     * Fornece dados para o componente DataTables na página de listagem de usuários.
     *
     * @param draw        O contador de sorteio.
     * @param start       O índice do registro inicial.
     * @param length      O número de registros a serem exibidos.
     * @param searchValue O valor de pesquisa global.
     * @param orderCol    O índice da coluna a ser ordenada.
     * @param orderDir    A direção da ordenação.
     * @return Um {@link DataTableResult} contendo os dados para a tabela.
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
        return usuarioService.dataTableUsuarios(params);
    }

    /**
     * Exibe a página principal para gerenciamento de usuários.
     *
     * @return Um {@link ModelAndView} para a página de índice de usuários.
     */
    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("usuarios/index");
    }

    /**
     * Exibe o formulário para criar um novo usuário.
     *
     * @param usuario Um {@link Usuario} vazio para vincular ao formulário.
     * @return Um {@link ModelAndView} para o formulário de criação de usuário.
     */
    @GetMapping("/novo")
    public ModelAndView novo(@ModelAttribute("usuario") Usuario usuario) {
        return new ModelAndView("usuarios/form");
    }

    /**
     * Exibe o formulário para editar um usuário existente.
     *
     * @param id O ID do usuário a ser editado.
     * @return Um {@link ModelAndView} para o formulário de edição de usuário.
     */
    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable("id") Long id) {
        ModelAndView model = new ModelAndView("usuarios/form");
        usuarioService.findById(id).ifPresent(usuario -> model.addObject("usuario", usuario));
        return model;
    }

    /**
     * Salva um novo usuário ou atualiza um existente.
     *
     * @param usuario O {@link Usuario} a ser salvo.
     * @param result  O resultado da validação.
     * @return Um {@link ModelAndView} para fechar o modal em caso de sucesso, ou de volta ao formulário em caso de erro.
     */
    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return novo(usuario);
        }
        usuarioService.save(usuario);
        return new ModelAndView("redirect:/usuarios");
    }

    /**
     * Exclui um usuário pelo seu ID.
     *
     * @param id O ID do usuário a ser excluído.
     * @return Um {@link ResponseEntity} com status 200 (OK) em caso de sucesso.
     */
    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<String> excluir(@PathVariable("id") Long id) {
        if (usuarioService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.deleteById(id);
        return ResponseEntity.ok("OK");
    }
}
