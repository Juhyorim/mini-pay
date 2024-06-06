package com.lime.minipay.filter;

import com.lime.minipay.entity.Member;
import com.lime.minipay.service.JwtService;
import com.lime.minipay.service.MemberService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@RequiredArgsConstructor
@Component
@Slf4j
public class LoginFilter implements Filter {
    private final JwtService jwtService;
    private final MemberService memberService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String contextPath = httpServletRequest.getRequestURI();

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<String> paths = List.of(
                "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/api/login/**", "/error",
                "/api/v1/boards/popular", "/api/v1/users/nickname/*/exists", "/api/v1/token/reissue",
                "/swagger-ui/index.html",
                "/member/signup",
                "/member/login", "/favicon**"
        );

        if (matchesPattern(contextPath, paths, antPathMatcher)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = httpServletRequest.getHeader("Authorization");
            validateAccessToken(accessToken);
            Member member = memberService.getUserByLoginId(jwtService.getMemberLoginId(accessToken));

            request.setAttribute("member", member);
            chain.doFilter(request, response);
        } catch (Exception e) {
            //pass
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void validateAccessToken(String accessToken) {
        //클라이언트 API 요청이 오면 Authrization header에 담긴 accessToken의 유효성을 검사
        //만약 유효하지 않은 토큰이 오면 403을 반환
        if (accessToken == null || jwtService.isExpired(accessToken)) {
            throw new RuntimeException("유효하지 않은 토큰");
        }
    }

    private boolean matchesPattern(String path, List<String> patterns, AntPathMatcher matcher) {
        for (String pattern : patterns) {
            if (matcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
