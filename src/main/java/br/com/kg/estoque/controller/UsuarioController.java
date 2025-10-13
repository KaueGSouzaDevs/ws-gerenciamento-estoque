package br.com.kg.estoque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controlador responsável por gerenciar as requisições relacionadas aos usuários.
 * Atualmente, lida apenas com a exibição da página principal de gerenciamento de usuários.
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    
    /**
     * Exibe a página principal para gerenciamento de usuários.
     *
     * @return Um objeto {@link ModelAndView} para a página de índice de usuários.
     */
    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("usuarios/index");
    }
}
