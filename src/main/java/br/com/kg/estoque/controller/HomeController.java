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

/**
 * Controlador principal que gerencia as páginas iniciais e de utilidades da aplicação.
 * Lida com a navegação para a página inicial, página de acesso negado e funcionalidades da interface do usuário.
 */
@Controller
public class HomeController {

    private final Logger logger = Logger.getLogger(HomeController.class.getName());
    private final SessionParameters sessionParameters;

    /**
     * Constrói um novo HomeController com os parâmetros de sessão especificados.
     *
     * @param sessionParameters Os parâmetros de sessão para gerenciar o estado da interface do usuário.
     */
    public HomeController(SessionParameters sessionParameters) {
        this.sessionParameters = sessionParameters;
    }

    /**
     * Retorna a página inicial da aplicação.
     * Realiza o log das informações de autenticação do usuário.
     *
     * @return Um {@link ModelAndView} para a página inicial.
     */
    @GetMapping("/")
    public ModelAndView index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.log(Level.ALL, () -> "Principal:"+ auth.getPrincipal());
        auth.getAuthorities().forEach(autho -> logger.log(Level.ALL, () -> "Authority: " + autho.getAuthority()));

        return new ModelAndView("home/index");
    }

    /**
     * Retorna a página de "acesso negado".
     * <p>
     * Essa página é exibida quando um usuário tenta acessar um recurso
     * para o qual não possui permissão.
     *
     * @return Um {@link ModelAndView} para a página de acesso negado.
     */
    @GetMapping("/acesso-negado")
    public ModelAndView acessoNegado() {
        return new ModelAndView("home/acesso-negado");
    }

    /**
     * Alterna a visibilidade da barra lateral (sidebar).
     * <p>
     * Este método é chamado via AJAX para alternar entre os estados "aberto" e "fechado"
     * da barra lateral da interface do usuário. O estado é persistido nos parâmetros da sessão.
     *
     * @return Uma {@link ResponseEntity} com status 200 (OK) para indicar que a operação foi bem-sucedida.
     */
    @GetMapping("/funcaoMenu")
    public ResponseEntity<String> funcaoMenu(){
        sessionParameters.setSidebarAberto(!sessionParameters.isSidebarAberto());
        return ResponseEntity.ok().build();
    }

}
