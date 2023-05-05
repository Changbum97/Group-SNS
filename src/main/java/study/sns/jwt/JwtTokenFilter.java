package study.sns.jwt;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import study.sns.domain.entity.User;
import study.sns.domain.exception.AppException;
import study.sns.domain.exception.ErrorCode;
import study.sns.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final Long accessTokenDurationSec;
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = null;
            String refreshToken = null;

            // API 인증 시 Token 꺼내기
            if (request.getHeader("ACCESS-TOKEN") != null ||
                    request.getHeader("REFRESH-TOKEN") != null) {
                accessToken = request.getHeader("ACCESS-TOKEN") == null ? null : request.getHeader("ACCESS-TOKEN");
                refreshToken = request.getHeader("REFRESH-TOKEN") == null ? null : request.getHeader("REFRESH-TOKEN");
            }
            // 화면 인증 시 Token 꺼내기
            else {
                if (request.getCookies() == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Cookie accessTokenCookie = Arrays.stream(request.getCookies())
                        .filter(cookie -> cookie.getName().equals("accessToken")).findFirst().orElse(null);
                Cookie refreshTokenCookie = Arrays.stream(request.getCookies())
                        .filter(cookie -> cookie.getName().equals("refreshToken")).findFirst().orElse(null);

                if (accessTokenCookie == null && refreshTokenCookie == null) {
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    if (accessTokenCookie != null) accessToken = "Bearer " + accessTokenCookie.getValue();
                    if (refreshTokenCookie != null) refreshToken = "Bearer " + refreshTokenCookie.getValue();
                }
            }

            if (accessToken != null) {

                if (JwtTokenUtil.isExpired(accessToken.split(" ")[1], secretKey)) {
                    throw new AppException(ErrorCode.TOKEN_EXPIRED);
                }

                if (!accessToken.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String loginId = JwtTokenUtil.getLoginId(accessToken.split(" ")[1], secretKey);
                User loginUser = userService.findByLoginId(loginId);

                // 권한 부여
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        loginUser.getLoginId(), null, List.of(new SimpleGrantedAuthority(loginUser.getUserRole().name())));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);

            } else if (refreshToken != null) {

                if (!refreshToken.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String token = refreshToken.split(" ")[1];
                if (JwtTokenUtil.isExpired(token, secretKey)) {
                    throw new AppException(ErrorCode.TOKEN_EXPIRED);
                }

                String loginId = JwtTokenUtil.getLoginId(token, secretKey);
                User loginUser = userService.findByLoginId(loginId);

                // Refresh Token 검증
                ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
                if (!stringStringValueOperations.get(loginId + "_refreshToken").equals(token)) {
                    throw new AppException(ErrorCode.INVALID_TOKEN);
                }

                // Refresh Token이 유효하면 Access Token을 다시 생성
                Cookie cookie = new Cookie("accessToken",
                        JwtTokenUtil.createToken(loginId, secretKey, accessTokenDurationSec * 1000));
                cookie.setMaxAge(Math.toIntExact(accessTokenDurationSec));
                cookie.setPath("/");
                response.addCookie(cookie);

                // 권한 부여
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        loginUser.getLoginId(), null, List.of(new SimpleGrantedAuthority(loginUser.getUserRole().name())));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);

            } else {
                filterChain.doFilter(request, response);
            }
        } catch (MalformedJwtException e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
