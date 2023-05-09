package study.sns.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.sns.domain.dto.user.oauth.UserDetail;
import study.sns.domain.exception.SecurityExceptionHandler;
import study.sns.filter.JwtTokenFilter;
import study.sns.util.JwtTokenUtil;
import study.sns.service.CustomOauth2UserService;
import study.sns.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.token.secret}")
    private String secretKey;
    @Value("${jwt.duration.access-token}")
    private Long accessTokenDurationSec;
    @Value("${jwt.duration.refresh-token}")
    private Long refreshTokenDurationSec;
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;
    private final CustomOauth2UserService customOauth2UserService;

    // 로그인하지 않은 유저들만 접근 가능한 URL
    private static final String[] anonymousUserUrl =
            {"/users/login", "/users/join", "/api/users/login", "/api/users/join", "/api/users/check-email-auth",
            "/api/users/send-auth-email", "/users/find-id-pw"};

    // 로그인한 유저들만 접근 가능한 URL
    private static final String[] authenticatedUserUrl =
            {"/api/users/logout", "/users/set-nickname", "/api/users/test", "/api/users/access-token",
                    "/shops/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HttpServletResponse response) throws Exception {

        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers(anonymousUserUrl).anonymous()
                .antMatchers(authenticatedUserUrl).authenticated()
                .antMatchers("/test").authenticated()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                // 인가 실패
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        String uri = request.getRequestURI();
                        PrintWriter pw = response.getWriter();

                        // 로그인 한 유저가 login, join, find-id-pw을 시도한 경우
                        if (uri.contains("login") || uri.contains("join") || uri.contains("find-id-pw")) {
                            // 메세지 출력 후 홈으로 redirect
                            response.setContentType("text/html");
                            pw.println("<script>alert('이미 로그인 되어있습니다!'); location.href='/';</script>");
                            pw.flush();
                        }
                    }
                })
                // 인증 실패
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        String uri = request.getRequestURI();
                        PrintWriter pw = response.getWriter();

                        // 메세지 출력 후 홈으로 redirect
                        response.setContentType("text/html");
                        pw.println("<script>alert('로그인한 유저만 접속 가능합니다!'); location.href='/users/login';</script>");
                        pw.flush();
                    }
                })
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(
                        new JwtTokenFilter(secretKey, accessTokenDurationSec, userService, stringRedisTemplate),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SecurityExceptionHandler(), JwtTokenFilter.class)
                .oauth2Login()
                    .loginPage("/users/login")
                    .failureUrl("/users/login")
                    .userInfoEndpoint().userService(customOauth2UserService)
                    .and()
                    .successHandler(
                            new AuthenticationSuccessHandler() {
                                @Override
                                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                    UserDetail userDetail = (UserDetail) authentication.getPrincipal();

                                    String accessToken = JwtTokenUtil.createToken(userDetail.getUsername(), secretKey, accessTokenDurationSec * 1000);
                                    String refreshToken = JwtTokenUtil.createToken(userDetail.getUsername(), secretKey, refreshTokenDurationSec * 1000);

                                    Cookie cookie1 = new Cookie("accessToken", accessToken);
                                    cookie1.setMaxAge(Math.toIntExact(accessTokenDurationSec));
                                    cookie1.setPath("/");
                                    response.addCookie(cookie1);

                                    Cookie cookie2 = new Cookie("refreshToken", refreshToken);
                                    cookie2.setMaxAge(Math.toIntExact(accessTokenDurationSec));
                                    cookie2.setPath("/");
                                    response.addCookie(cookie2);

                                    // Redis에 Refresh Token 저장
                                    stringRedisTemplate.delete(userDetail.getUsername() + "_refreshToken");
                                    ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
                                    stringStringValueOperations.set(userDetail.getUsername() + "_refreshToken", refreshToken,
                                            refreshTokenDurationSec, TimeUnit.SECONDS);

                                    PrintWriter pw = response.getWriter();
                                    if (userDetail.getNickname().equals("")) {
                                        response.setContentType("text/html");
                                        pw.println("<script>alert('회원가입에 성공하였습니다!\\n닉네임을 설정해주세요.'); location.href='/users/set-nickname';</script>");
                                        pw.flush();
                                    } else {
                                        response.setContentType("text/html");
                                        pw.println("<script>alert('" + userDetail.getNickname() + "님 반갑습니다!'); location.href='/';</script>");
                                        pw.flush();
                                    }
                                }
                            }
                    )
                .and()
                .build();

    }
}
