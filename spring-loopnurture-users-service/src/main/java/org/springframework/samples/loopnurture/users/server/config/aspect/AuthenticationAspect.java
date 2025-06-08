package org.springframework.samples.loopnurture.users.server.config.aspect;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.exception.UnauthorizedException;
import org.springframework.samples.loopnurture.users.infra.context.UserContext;
import org.springframework.samples.loopnurture.users.server.config.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证切面
 * 处理需要登录的接口，验证JWT token并设置用户上下文
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final JwtUtils jwtUtils;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 处理需要登录的接口
     * 使用@RequireLogin注解标记需要登录的接口
     */
    @Around("@annotation(org.springframework.samples.loopnurture.users.server.config.annotation.RequireLogin)")
    public Object handleAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 获取token并验证
            String token = extractToken();
            
            if (!jwtUtils.validateToken(token)) {
                throw new UnauthorizedException("Invalid or expired token");
            }
            
            // 解析token中的用户信息
            Claims claims = jwtUtils.getClaimsFromToken(token);
            
            // 设置用户上下文
            UserContext context = new UserContext();
            context.setSystemUserId(Long.parseLong(claims.getSubject()));
            context.setUserUniq((String) claims.get("userUniq"));
            context.setAuthType(AuthTypeEnum.valueOf((String) claims.get("authType")));
            context.setOrganizationId(jwtUtils.getOrganizationIdFromToken(token));
            UserContext.set(context);
            
            // 执行实际的业务方法
            return joinPoint.proceed();
        } finally {
            // 清理用户上下文
            UserContext.clear();
        }
    }

    /**
     * 从请求头中提取token
     */
    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader(AUTH_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        return authHeader.substring(BEARER_PREFIX.length());
    }
} 