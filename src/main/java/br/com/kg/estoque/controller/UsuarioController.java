package br.com.kg.estoque.controller;

import java.time.LocalDateTime;
import java.util.List;

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
import br.com.kg.estoque.domain.grupo_acesso.GrupoAcesso;
import br.com.kg.estoque.domain.grupo_acesso.GrupoAcessoService;
import br.com.kg.estoque.domain.usuario.Usuario;
import br.com.kg.estoque.domain.usuario.UsuarioService;
import br.com.kg.estoque.domain.usuario.UsuarioValidation;
import br.com.kg.estoque.enuns.SituacaoUsuario;
import jakarta.validation.Valid;

/**
 * Controlador responsável por gerenciar as requisições relacionadas aos usuários.
 * Lida com as operações de CRUD e listagem de dados para a entidade de usuário.
 */
@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('GERENCIAMENTO_USUARIOS')")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioValidation usuarioValidation;
    private final GrupoAcessoService grupoAcessoService;

    /**
     * Constrói um novo UsuarioController com os serviços especificados.
     *
     * @param usuarioService     O serviço para lidar com a lógica de negócios do usuário.
     * @param grupoAcessoService O serviço para lidar com a lógica de negócios do grupo de acesso.
     */
    public UsuarioController(UsuarioService usuarioService, UsuarioValidation usuarioValidation, GrupoAcessoService grupoAcessoService) {
        this.usuarioService = usuarioService;
        this.usuarioValidation = usuarioValidation;
        this.grupoAcessoService = grupoAcessoService;
    }

    /**
     * Fornece a lista de todos os grupos de acesso para os modelos.
     *
     * @return Uma lista de {@link GrupoAcesso}.
     */
    @ModelAttribute("gruposAcessos")
    public List<GrupoAcesso> allGruposAcessos() {
        return grupoAcessoService.findAll(false);
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
        ModelAndView model = new ModelAndView("usuarios/form");
        model.addObject("situacaoList", SituacaoUsuario.values());
        return model;
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
        model.addObject("situacaoList", SituacaoUsuario.values());
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

        usuarioValidation.validarNome(usuario, result);
        usuarioValidation.validarEmail(usuario, result);

        if (result.hasErrors()) {
            return novo(usuario);
        }

        if (usuario.getId() != null) {
            usuario.setDataAtualizacao(LocalDateTime.now());
        } else {
            usuarioService.gerarLogin(usuario);
            usuario.setSituacaoUsuario(SituacaoUsuario.NOVO);
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
