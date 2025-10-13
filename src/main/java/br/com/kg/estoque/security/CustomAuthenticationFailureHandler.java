package br.com.kg.estoque.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Manipulador customizado para falhas de autenticação.
 * <p>
 * Esta classe implementa a interface {@link AuthenticationFailureHandler} do Spring Security
 * para definir o comportamento da aplicação quando um usuário não consegue fazer login.
 * Sua principal responsabilidade é redirecionar o usuário de volta para a página de login
 * com um parâmetro de erro, permitindo que a interface exiba uma mensagem apropriada
 * (ex: 'Usuário ou senha inválidos').
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * Chamado pelo Spring Security quando uma tentativa de autenticação falha.
     * Redireciona a requisição para a URL '/login?error'.
     *
     * @param request a requisição HTTP onde a falha de autenticação ocorreu.
     * @param response a resposta HTTP.
     * @param exception a exceção que causou a falha na autenticação (não utilizada nesta implementação, mas disponível para logging ou lógica mais complexa).
     * @throws IOException se ocorrer um erro de entrada/saída durante o redirecionamento.
     * @throws ServletException se ocorrer um erro interno do servlet.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {                    
        response.sendRedirect(request.getContextPath()+"/login?error");
    }
    
}
