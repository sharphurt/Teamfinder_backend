package ru.catstack.auth.security.jwt;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.catstack.auth.exception.JwtAuthenticationException;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.User;
import ru.catstack.auth.security.JwtUserDetailsService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long validityInMilliseconds;

    @Value("${app.jwt.header.prefix}")
    private String tokenPrefix;

    @Value("${app.jwt.header}")
    private String tokenHeader;

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

    public String createToken(@NotNull User customUserDetails) {
        Instant expiryDate = Instant.now().plusMillis(validityInMilliseconds);
        return Jwts.builder()
                .setId(Long.toString(customUserDetails.getId()))
                .setSubject(customUserDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    String resolveToken(@NotNull HttpServletRequest req) {
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
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    @NotNull
    private List<String> getRoleNames(List<Role> userRoles) {
        List<String> result = new ArrayList<>();

        userRoles.forEach(role -> result.add(role.getRole().name()));
        return result;
    }

    public Long getValidityDuration() {
        return validityInMilliseconds;
    }

}
