package br.com.kg.estoque.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.kg.estoque.session.SessionParameters;


@Controller
public class HomeController {

    Logger logger = Logger.getLogger(HomeController.class.getName());
    private SessionParameters sessionParameters;



    /**
     * Retorna a página inicial da aplicação.
     * 
     * @return View da página inicial
     */
    @GetMapping("/")
    public ModelAndView index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.log(Level.ALL, () -> "Principal:"+ auth.getPrincipal());
        auth.getAuthorities().forEach(autho -> logger.log(Level.ALL, () -> "Authority: " + autho.getAuthority()));

        return new ModelAndView("home/index");
    }



    /**
     * Retorna a página de acesso negado.
     * <p>
     * Essa página é retornada quando o usuário tenta acessar uma URL que
     * ele não tem permissão.
     * 
     * @return View da página de acesso negado
     */
    @GetMapping("/acessoNegado")
    public ModelAndView acessoNegado() {
        return new ModelAndView("home/acesso-negado");
    }
    



    /**
     * Alterna o estado de abertura da barra lateral.
     * <p>
     * Este método é chamado para alternar entre os estados aberto e fechado da barra lateral
     * da interface do usuário. Ele modifica o estado atual e retorna uma resposta HTTP 200 (OK).
     *
     * @return Resposta HTTP 200 (OK) indicando que a operação foi bem-sucedida.
     */
    @GetMapping("/funcaoMenu")
    public ResponseEntity<String> funcaoMenu(){
        sessionParameters.setSidebarAberto(!sessionParameters.isSidebarAberto());
        return ResponseEntity.ok().build();
    }

}
