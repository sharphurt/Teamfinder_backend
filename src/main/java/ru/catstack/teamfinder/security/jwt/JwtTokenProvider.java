package ru.catstack.teamfinder.security.jwt;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.catstack.teamfinder.exception.JwtAuthenticationException;
import ru.catstack.teamfinder.model.User;
import ru.catstack.teamfinder.security.JwtUserDetailsService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static ru.catstack.teamfinder.config.SecurityConfig.*;

@Component
public class JwtTokenProvider {
    public static String secret = JWT_SECRET_WORD;

    public static long validityInMilliseconds = JWT_EXPIRATION;

    public static String tokenPrefix = JWT_PREFIX;

    public static String tokenHeader = JWT_HEADER;

    private final JwtUserDetailsService userDetailsService;

    public JwtTokenProvider(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(@NotNull User user) {
        Instant expiryDate = Instant.now().plusMillis(validityInMilliseconds);
        return Jwts.builder()
                .setId(Long.toString(user.getId()))
                .setSubject(user.getRoles().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    Authentication getAuthentication(String token) {
        var userDetails = userDetailsService.loadById(getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId());
    }

    public String resolveToken(@NotNull HttpServletRequest req) {
        String bearerToken = req.getHeader(tokenHeader);
        if (bearerToken != null && bearerToken.startsWith(tokenPrefix + " ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    public Long getValidityDuration() {
        return validityInMilliseconds;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }
}
