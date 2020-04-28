package ru.catstack.teamfinder.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.catstack.teamfinder.exception.InvalidJwtTokenException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtTokenProvider.resolveToken(request);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                Long userId = jwtTokenProvider.getUserId(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            throw new InvalidJwtTokenException("");
        }

        filterChain.doFilter(request, response);
    }



//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
//        try {
//            String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
//            if (token != null && jwtTokenProvider.validateToken(token)) {
//                Authentication auth = jwtTokenProvider.getAuthentication(token);
//
//                if (auth != null) {
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                }
//            }
//        } catch (Exception ex) {
//            res.se
//        }
//        filterChain.doFilter(req, res);
//    }
}
