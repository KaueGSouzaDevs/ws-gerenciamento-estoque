package br.com.kg.estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import jakarta.validation.Valid;

@Controller
@RequestMapping("/grupos-acessos")
@PreAuthorize("hasRole('GERENCIAMENTO_GRUPOS_ACESSOS')")
public class GrupoAcessoController {

    @Autowired
    private GrupoAcessoService grupoAcessoService;



    @GetMapping("")
    public ModelAndView listagemDeGrupos() {
        return new ModelAndView("grupo-acessos/index");
    }



    @GetMapping("/dataTable")
    @ResponseBody
    public DataTableResult jsonDataTable(
            @RequestParam("draw") String draw,
            @RequestParam("start") Integer start,
            @RequestParam("length") Integer length,
            @RequestParam("search[value]") String searchValue,
            @RequestParam("order[0][column]") Integer orderCol,
            @RequestParam("order[0][dir]") String orderDir) {
        return grupoAcessoService.dataTableGrupoAcessos(new DataTableParams(draw, start, length, searchValue, orderCol, orderDir));
    }



    @GetMapping("/novo")
    public ModelAndView novo(@ModelAttribute("grupoAcesso") GrupoAcesso grupoAcesso) {
        return new ModelAndView("grupo-acessos/form");
    }



    @GetMapping("/{id}/editar")
    public ModelAndView editar(@ModelAttribute("grupoAcesso") GrupoAcesso grupoAcesso) {
        ModelAndView model = new ModelAndView("grupo-acessos/form");
        grupoAcesso = grupoAcessoService.buscarPorId(grupoAcesso.getId()).orElse(null);
        model.addObject("grupoAcesso", grupoAcesso);
        return model;
    }



    @PostMapping("/salvar")
    public ModelAndView salvar(@Valid GrupoAcesso grupoAcesso, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView model = new ModelAndView("grupo-acessos/form");
            model.addObject("grupoAcesso", grupoAcesso);
            return model;
        }

        grupoAcessoService.save(grupoAcesso);
        return new ModelAndView("redirect:/grupos-acessos");
    }



    @DeleteMapping("/{id}/excluir")
    public ResponseEntity<String> excluir(@PathVariable("id") Long id) {
        try {
            grupoAcessoService.excluir(id);
            return ResponseEntity.ok().body("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

}
