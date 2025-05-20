package br.com.imersao_app.demo.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.imersao_app.demo.entity.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    @Value("${jwt.issuer}")
    private String issuer;
    
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);


    // Método para obter a chave de assinatura
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Geração de token básico
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Geração de token com claims extras
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, accessTokenExpiration);
    }

    // Geração de refresh token
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    // Método principal para construção de tokens
    private String buildToken(Map<String, Object> extraClaims, 
                             UserDetails userDetails, 
                             long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extração de username (email)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extração de role
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Extração de userId
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    // Extração de data de expiração
    public Long getExpirationInMillis(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null ? expiration.getTime() : null;
    }

    // Extração genérica de claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Validação básica de token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Validação específica para refresh token
    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            return !isTokenExpired(refreshToken) && 
                   extractClaim(refreshToken, Claims::getIssuer).equals(issuer);
        } catch (JwtException ex) {
            return false;
        }
    }

    // Verificação de token expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (ExpiredJwtException ex) {
            throw new JwtException("Token expirado", ex);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            throw new JwtException("Token inválido", ex);
        } catch (IllegalArgumentException ex) {
            throw new JwtException("Token vazio ou nulo", ex);
        }
    }

    // Extração de todos os claims com tratamento de erros
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .requireIssuer(issuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new JwtException("Token expirado", ex);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException ex) {
            throw new JwtException("Token inválido", ex);
        } catch (IllegalArgumentException ex) {
            throw new JwtException("Token vazio ou nulo", ex);
        }
    }

    // Método específico para usuários da aplicação
    public String gerarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", usuario.getRole().name());
        claims.put("userId", usuario.getId());
        
        // ⚠️ Adicione um log para depuração:
        log.info("Gerando token para usuário: {}", usuario.getEmail());
        
        try {
            String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey())
                .compact();
            
            log.info("Token gerado com sucesso");
            return token;
        } catch (Exception e) {
            log.error("Falha ao gerar token", e);
            return null; // Isso causaria o erro! Remova se existir
        }
    }

    // Método para extrair todos os claims como mapa
    public Map<String, Object> extractAllClaimsAsMap(String token) {
        return new HashMap<>(extractAllClaims(token));
    }
    
    public Date extractExpirationTime(String token) {
        return extractExpiration(token);
    }
    
}

