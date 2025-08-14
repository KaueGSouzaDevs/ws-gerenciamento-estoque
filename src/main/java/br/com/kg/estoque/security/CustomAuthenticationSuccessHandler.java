package br.com.kg.estoque.security;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import br.com.kg.estoque.domain.usuario.Usuario;
import br.com.kg.estoque.session.SessionParameters;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ApplicationContext applicationContext;
    
    public CustomAuthenticationSuccessHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }



    /**
     * 
     * Implementa o método de sucesso de autenticação, verificando se a conta está resetada ou nova.
     * Se sim, redireciona para a página de reset de senha.
     * Caso contrário, seta o ano no SessionParameters e redireciona para a página inicial.
     * @param request - O request da requisição.
     * @param response - A resposta da requisição.
     * @param authentication - O objeto de autenticação.
     * @throws IOException - Exceção de Entrada/Saída.
     * @throws ServletException - Exceção de Servlet.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Usuario usuario = (Usuario) authentication.getPrincipal();

        if(usuario.isContaResetada() || usuario.isNovaConta()){
            response.sendRedirect(request.getContextPath()+"/login/formReset");
            return;
        }

        SessionParameters sessionParameters = applicationContext.getBean(SessionParameters.class);
        LocalDate now = LocalDate.now();
        sessionParameters.setAno(now.getYear());
        response.sendRedirect(request.getContextPath());
    }
    
}
