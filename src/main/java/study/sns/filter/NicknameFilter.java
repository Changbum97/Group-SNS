package study.sns.filter;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import study.sns.service.UserService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class NicknameFilter implements Filter {

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 닉네임 설정 페이지, CSS 통과
        String uri = ((HttpServletRequest)request).getRequestURI();
        if(uri.contains("nickname") || uri.contains("css") || uri.contains("logout")) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = null;
        if (((HttpServletRequest) request).getCookies() != null) {
            for (Cookie cookie : ((HttpServletRequest) request).getCookies()) {
                if (cookie.getName().equals("accessToken")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        // 로그인 안 되어있으면 통과
        if (accessToken == null) {
            chain.doFilter(request, response);
            return;
        }

        // nickname이 ""라면 Redirect
        if(userService.findByAccessToken(accessToken).getNickname().equals("")) {
            ((HttpServletResponse) response).sendRedirect("/users/set-nickname");
            return;
        }

        chain.doFilter(request, response);
    }
}
