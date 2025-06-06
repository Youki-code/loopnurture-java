package org.springframework.samples.loopnurture.users.server.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.samples.loopnurture.users.infra.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT认证拦截器
 */
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取token
        String authHeader = request.getHeader(AUTH_HEADER);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 验证token
        String token = authHeader.substring(BEARER_PREFIX.length());
        if (!jwtUtils.validateToken(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        // 将用户ID添加到请求属性中，供后续使用
        request.setAttribute("systemUserId", jwtUtils.getSystemUserIdFromToken(token));
        return true;
    }
} 