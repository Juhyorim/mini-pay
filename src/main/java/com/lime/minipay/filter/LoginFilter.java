package com.lime.minipay.filter;

import com.lime.minipay.service.JwtService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class LoginFilter implements Filter {
    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String contextPath = httpServletRequest.getRequestURI();
        if (contextPath.equals("/member/signup") || contextPath.equals("/member/login")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            validateAccessToken(httpServletRequest.getHeader("Authorization"));
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
}
