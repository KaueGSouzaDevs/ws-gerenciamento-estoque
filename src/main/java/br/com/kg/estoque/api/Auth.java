package br.com.kg.estoque.api;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.kg.estoque.custom.AuthUser;
import br.com.kg.estoque.domain.usuario.Usuario;
import br.com.kg.estoque.domain.usuario.UsuarioService;
import br.com.kg.estoque.security.AutenticacaoService;
import br.com.kg.estoque.services.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class Auth {

    private final UsuarioService usuarioService;
    private final AutenticacaoService autenticacaoService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public Auth(UsuarioService usuarioService, AutenticacaoService autenticacaoService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.autenticacaoService = autenticacaoService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    // @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthUser user) throws UsernameNotFoundException {
        // Busca o usuário pelo login
        Optional<Usuario> usuarioOptional = usuarioService.findByLogin(user.login());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuário ou senha inválidos!"));
        }

        // Verifica se o login e senha estão corretos
        UserDetails userDetails = autenticacaoService.loadUserByUsername(user.login());
        if (!passwordEncoder.matches(user.senha(), userDetails.getPassword())) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuário ou senha inválidos!"));
        }

        Usuario usuario = usuarioOptional.get();
        if (!usuario.isEnabled()) {
            return ResponseEntity.status(401).body(Map.of("error", "Conta bloqueada, por gentileza entre em contato com o suporte."));
        }

        if (usuario.isContaResetada()) {
            return ResponseEntity.status(405).body(Map.of("error", "Senha alterada com sucesso!<br/>Informe seus dados para acessar o sistema!"));
        }

        // Gera um token JWT
        String token = jwtService.gerarToken(usuario.getId(), usuario.getNome(), userDetails.getAuthorities().stream().map(role -> new String(role.toString())).toList());

        // Retorna o token no corpo da resposta
        return ResponseEntity.ok(Map.of("token", token));
    }


}
