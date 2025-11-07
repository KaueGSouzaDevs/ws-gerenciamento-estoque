package br.com.kg.estoque.api.exception;


import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import br.com.kg.estoque.common.MensagemErro;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import javassist.NotFoundException;

@RestControllerAdvice(basePackages = "br.com.kg.estoque.api")
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * Trata exceções de violação de constraints de validação.
     * Essa exceção é lançada quando validações JPA/Bean Validation falham,
     * geralmente em operações de entidades ou parâmetros de métodos.
     * 
     * @param ex A exceção de violação de constraints capturada
     * @return ResponseEntity com código 400 (Bad Request) e mensagem de erro detalhada
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MensagemErro> handleConstraintViolationException(ConstraintViolationException ex) {
        
        // Coleta todas as mensagens de violação de constraints e as concatena
        String mensagem = ex.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining("; "));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensagemErro(400, mensagem));
    }


    /**
     * Trata exceções de argumentos inválidos em métodos de controllers.
     * Esta exceção é lançada quando dados enviados no request não passam
     * nas validações definidas nos DTOs (@Valid, @NotNull, @Size, etc.).
     * 
     * @param ex A exceção de argumentos inválidos capturada
     * @return ResponseEntity com código 400 (Bad Request) e mensagem de erro com detalhes dos campos inválidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensagemErro> handleValidationException(MethodArgumentNotValidException ex) {
        // Extrai as mensagens de erro de cada campo inválido e as concatena
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensagemErro(400, mensagem));
    }

    
    /**
     * Trata todas as exceções não capturadas pelos handlers específicos.
     * Este é o manipulador genérico que funciona como uma rede de segurança
     * para qualquer exceção não prevista que possa ocorrer na aplicação.
     * 
     * @param ex A exceção genérica capturada
     * @return ResponseEntity com código 500 (Internal Server Error) e mensagem de erro genérica
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensagemErro> handleGenericException(Exception ex) {
        // Log do erro para análise posterior pelos desenvolvedores
        logger.error("Erro inesperado: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensagemErro(500, "Erro interno no servidor"));
    }
    
    /**
     * Trata exceções de conflito de negócio da aplicação.
     * Esta exceção é lançada quando ocorrem situações de conflito específicas
     * da regra de negócio, como tentativas de criação de recursos duplicados
     * ou operações que violam regras de negócio definidas.
     * 
     * @param ex A exceção de conflito capturada
     * @return ResponseEntity com código 409 (Conflict) e mensagem de erro específica
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<MensagemErro> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MensagemErro(409, ex.getMessage()));
    }

    /**
     * Trata exceções de violação de integridade de dados do banco.
     * Esta exceção é lançada pelo Spring Data quando operações de banco de dados
     * violam constraints de integridade, como chaves estrangeiras, valores únicos,
     * ou outras restrições definidas no schema do banco de dados.
     * 
     * @param ex A exceção de violação de integridade de dados capturada
     * @return ResponseEntity com código 409 (Conflict) e mensagem de erro padronizada
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<MensagemErro> handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MensagemErro(409, "Violação de integridade de dados."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MensagemErro> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensagemErro(400, "Corpo da requisição inválido ou malformado."));
    }

    /**
     * Trata exceções de recurso não encontrado.
     * Esta exceção deve ser lançada pelos serviços quando uma entidade
     * ou recurso específico não é encontrado no sistema.
     *
     * @param ex A exceção de recurso não encontrado capturada.
     * @return ResponseEntity com código 404 (Not Found) e a mensagem da exceção.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MensagemErro> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensagemErro(404, ex.getMessage()));
    }

    /**
     * Trata exceções de acesso negado do Spring Security.
     * Lançada quando um usuário autenticado tenta acessar um recurso
     * para o qual não tem permissão.
     *
     * @param ex A exceção de acesso negado capturada.
     * @return ResponseEntity com código 403 (Forbidden) e mensagem de erro.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<MensagemErro> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MensagemErro(403, "Acesso negado. Você não tem permissão para executar esta operação."));
    }

    /**
     * Trata exceções de tipo de argumento de método inválido.
     * Ocorre quando o tipo de um parâmetro de método (ex: @PathVariable, @RequestParam)
     * não corresponde ao esperado, como passar um texto onde se espera um número.
     *
     * @param ex A exceção de tipo de argumento inválido capturada.
     * @return ResponseEntity com código 400 (Bad Request) e mensagem de erro detalhada.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MensagemErro> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String mensagem = String.format("O parâmetro '%s' recebeu o valor '%s', que é de um tipo inválido. O tipo esperado é '%s'.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MensagemErro(400, mensagem));
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<MensagemErro> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MensagemErro(404, ex.getMessage()));
    }
}
