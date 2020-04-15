package ru.catstack.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import ru.catstack.auth.advice.CustomAccessDeniedHandler;
import ru.catstack.auth.security.jwt.JwtConfigurer;
import ru.catstack.auth.security.jwt.JwtTokenProvider;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/api/admin/**";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String REGISTER_ENDPOINT = "/api/auth/register";
    private static final String REFRESH_ENDPOINT = "/api/auth/refresh";
    private static final String LOGOUT_ENDPOINT = "/api/auth/logout";
    private static final String USER_INFORMATION_ENDPOINT = "/api/user/aboutMe";
    private static final String SET_USER_INFORMATION_ENDPOINT = "/api/user/setAboutMe";
    private static final String UPLOAD_IMAGE_ENDPOINT = "/api/image/upload";
    private static final String GET_IMAGE_ENDPOINT = "/api/image/get";


    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(REGISTER_ENDPOINT).permitAll()
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .antMatchers(REFRESH_ENDPOINT).permitAll()
                .antMatchers(USER_INFORMATION_ENDPOINT).authenticated()
                .antMatchers(SET_USER_INFORMATION_ENDPOINT).authenticated()
                .antMatchers(LOGOUT_ENDPOINT).authenticated()
                .antMatchers(UPLOAD_IMAGE_ENDPOINT).authenticated()
                .antMatchers(GET_IMAGE_ENDPOINT).authenticated()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint());
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAccessDeniedHandler();
    }
}
