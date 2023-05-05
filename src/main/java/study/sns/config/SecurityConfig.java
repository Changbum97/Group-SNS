package study.sns.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.sns.domain.exception.SecurityExceptionHandler;
import study.sns.jwt.JwtTokenFilter;
import study.sns.service.UserService;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.token.secret}")
    private String secretKey;
    @Value("${jwt.duration.access-token}")
    private Long accessTokenDurationSec;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HttpServletResponse response) throws Exception {

        return httpSecurity
        .httpBasic().disable()
        .csrf().disable()
        .cors().and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/test").authenticated()
        .anyRequest().permitAll()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(
               new JwtTokenFilter(secretKey, accessTokenDurationSec, userService),
               UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new SecurityExceptionHandler(), JwtTokenFilter.class)
        .build();

    }
}
