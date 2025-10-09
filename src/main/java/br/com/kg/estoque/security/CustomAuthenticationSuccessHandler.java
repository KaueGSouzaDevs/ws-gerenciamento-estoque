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

/**
 * Manipulador customizado para o sucesso da autenticação.
 * <p>
 * Esta classe implementa a interface {@link AuthenticationSuccessHandler} do Spring Security
 * para definir o comportamento da aplicação após um login bem-sucedido.
 * A lógica principal verifica o estado da conta do usuário para determinar o redirecionamento apropriado.
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ApplicationContext applicationContext;
    
    /**
     * Constrói o manipulador com o contexto da aplicação.
     * O {@link ApplicationContext} é usado para obter beans gerenciados pelo Spring, como o {@link SessionParameters}.
     *
     * @param applicationContext O contexto da aplicação Spring.
     */
    public CustomAuthenticationSuccessHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Chamado pelo Spring Security quando uma tentativa de autenticação é bem-sucedida.
     * <p>
     * A lógica de negócio é a seguinte:
     * 1.  Verifica se a conta do usuário está com o status {@code RESETADO} ou {@code NOVO}.
     *     - Se estiver, o usuário é redirecionado para o formulário de alteração de senha.
     * 2.  Caso contrário, a conta é considerada normal.
     *     - O ano atual é definido nos parâmetros da sessão.
     *     - O usuário é redirecionado para a página inicial da aplicação.
     *
     * @param request a requisição HTTP.
     * @param response a resposta HTTP.
     * @param authentication o objeto {@link Authentication} que contém os detalhes do principal (usuário).
     * @throws IOException se ocorrer um erro de entrada/saída durante o redirecionamento.
     * @throws ServletException se ocorrer um erro interno do servlet.
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
