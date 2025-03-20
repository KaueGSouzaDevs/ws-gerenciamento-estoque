package br.com.kg.estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.kg.estoque.session.SessionParameters;


@Controller
public class HomeController {

    @Autowired
    private SessionParameters sessionParameters;

    @GetMapping("/")
    public ModelAndView index() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Principal:"+ auth.getPrincipal());
        auth.getAuthorities().forEach(System.out::println);

        return new ModelAndView("home/index");
    }

    @GetMapping("/acessoNegado")
    public ModelAndView acessoNegado() {
        return new ModelAndView("home/acesso-negado");
    }
    

    @GetMapping("/funcaoMenu")
    public ResponseEntity<?> funcaoMenu(){
        sessionParameters.setSidebarAberto(!sessionParameters.isSidebarAberto());
        return ResponseEntity.ok().build();
    }

}
