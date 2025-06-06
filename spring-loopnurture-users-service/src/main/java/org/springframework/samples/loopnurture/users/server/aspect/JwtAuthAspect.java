package org.springframework.samples.loopnurture.users.server.aspect;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.exception.UnauthorizedException;
import org.springframework.samples.loopnurture.users.infra.context.UserContext;
import org.springframework.samples.loopnurture.users.infra.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * JWT认证切面
 * 用于处理需要登录的接口
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class JwtAuthAspect {

    private final JwtUtils jwtUtils;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * 处理需要登录的接口
     * 使用@RequireLogin注解标记需要登录的接口
     */
    @Around("@annotation(org.springframework.samples.loopnurture.users.server.annotation.RequireLogin)")
    public Object handleAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 获取token并验证
            String token = getAndValidateToken();
            
            // 解析token中的用户信息
            Claims claims = jwtUtils.getClaimsFromToken(token);
            
            // 设置用户上下文
            UserContext context = new UserContext();
            context.setSystemUserId(Long.parseLong(claims.getSubject()));
            context.setUserUniq((String) claims.get("userUniq"));
            context.setAuthType(AuthTypeEnum.valueOf((String) claims.get("authType")));
            UserContext.set(context);
            
            // 执行实际的业务方法
            return joinPoint.proceed();
        } finally {
            // 清理用户上下文
            UserContext.clear();
        }
    }

    /**
     * 获取并验证token
     */
    private String getAndValidateToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader(AUTH_HEADER);
        
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("未登录或token已过期");
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        if (!jwtUtils.validateToken(token)) {
            throw new UnauthorizedException("token无效或已过期");
        }

        return token;
    }
} 