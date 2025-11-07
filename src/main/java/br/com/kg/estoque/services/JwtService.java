package br.com.kg.estoque.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtService {
    
    // 1. Defina seu "segredo". ISSO NÃO DEVE FICAR NO CÓDIGO!
    @Value("${jwt.secret}")
    private String SECRET;
    private final String ISSUER = "gerenciamento-geral"; // Quem está emitindo

    public String gerarToken(Long idUsuario, String nomeUsuario, List<String> permissoes) {

        // 2. Define o algoritmo de assinatura usando o segredo
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        // 3. Define o tempo de expiração (ex: 2 horas)
        Instant agora = Instant.now();
        Instant expiraEm = agora.plus(2, ChronoUnit.HOURS);

        // 4. Cria o JWT
        String token = JWT.create()
                .withIssuer(ISSUER) // Quem emite?
                .withSubject(idUsuario.toString())
                
                // Aqui você adiciona os "Claims" (informações públicas)
                .withClaim("nome", nomeUsuario) 
                
                // Resposta direta à sua necessidade: "e as permissões que ele tem"
                .withClaim("permissoes", permissoes) 

                .withIssuedAt(agora) // Quando foi criado
                .withExpiresAt(expiraEm) // Quando expira
                
                // 5. Assina o token
                .sign(algorithm);

        return token;
    }

}
