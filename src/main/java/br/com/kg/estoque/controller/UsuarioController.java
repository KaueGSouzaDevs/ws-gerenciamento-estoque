package br.com.kg.estoque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    

    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("usuarios/index");
    }
}
