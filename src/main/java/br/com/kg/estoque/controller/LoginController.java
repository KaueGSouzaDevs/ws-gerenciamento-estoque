package br.com.kg.estoque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador responsável por gerenciar o processo de login e logout da aplicação.
 */
@Controller
@RequestMapping("")
public class LoginController {
    
    /**
     * Exibe a página de login.
     * Antes de exibir a página, invalida qualquer sessão HTTP existente para garantir
     * um processo de login limpo.
     *
     * @param session A sessão HTTP atual, que será invalidada.
     * @return Um {@link ModelAndView} para o formulário de login.
     */
    @GetMapping("/login")
    public ModelAndView login(HttpSession session){
        session.invalidate();
        return new ModelAndView("login/form-login");
    }
}
