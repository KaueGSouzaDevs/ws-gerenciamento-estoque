package br.com.kg.estoque.domain.usuario;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

@Service
public class UsuarioValidation {

    private final UsuarioService usuarioService;

    public UsuarioValidation(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }



    public void validarNome(Usuario usuario, BindingResult result) {
        String nome = usuario.getName();

        // 1. Valida se o nome não é nulo ou vazio
        if (!StringUtils.hasText(nome)) {
            // A anotação @Size já deve cuidar disso, mas é uma boa prática garantir.
            return;
        }

        // 2. Remove espaços extras no início e no fim para a validação
        nome = nome.trim();

        // 3. Valida se o nome contém apenas letras, apóstrofos, hífens e espaços.
        // A regex `\p{L}` inclui caracteres acentuados.
        if (!nome.matches("^[\\p{L}'\\- ]+$")) {
            result.rejectValue("nome", "nome_error", "* O nome contém caracteres inválidos.");
            return; // Se tem caracteres inválidos, não adianta continuar
        }

        // 4. Valida se o nome é composto por pelo menos duas partes (nome e sobrenome)
        String[] partesNome = nome.split("\\s+");
        if (partesNome.length < 2) {
            result.rejectValue("nome", "nome_error", "* Insira o nome completo (nome e sobrenome).");
        } else {
            // 5. Valida se cada parte do nome tem um tamanho mínimo (ex: 2 caracteres)
            for (String parte : partesNome) {
                if (parte.length() < 2) {
                    result.rejectValue("nome", "nome_error", "* Nome ou sobrenome parece curto demais.");
                    break;
                }
            }
        }
    }

    public void validarEmail(Usuario usuario, BindingResult result) {
        if (!StringUtils.hasText(usuario.getEmail())) {
            return;
        }

        if (usuario.getId() != null) {
            if (usuarioService.findByEmailAndIdNot(usuario.getEmail(), usuario.getId()).isPresent()) {
                result.rejectValue("email", "email_error", "* Já existe um usuário cadastrado com este e-mail");
            }
        } else {
            if (usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
                result.rejectValue("email", "email_error", "* Já existe um usuário cadastrado com este e-mail");
            }
        }

    }
}
