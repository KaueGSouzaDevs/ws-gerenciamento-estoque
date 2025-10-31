package br.com.kg.estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.domain.grupo_acesso.GrupoAcessoService;

@Controller
@RequestMapping("/grupos-acessos")
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

}
