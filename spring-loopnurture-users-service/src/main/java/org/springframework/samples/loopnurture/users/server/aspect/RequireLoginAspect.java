package org.springframework.samples.loopnurture.users.server.aspect;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录验证切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequireLoginAspect {

    private final JwtUtils jwtUtils;

    /**
     * 验证用户是否登录
     */
    @Around("@annotation(org.springframework.samples.loopnurture.users.server.annotation.RequireLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = extractToken(request);
            
            if (!jwtUtils.validateToken(token)) {
                throw new UnauthorizedException("Invalid or expired token");
            }

            // 设置用户上下文
            UserContext context = new UserContext();
            context.setSystemUserId(jwtUtils.getSystemUserIdFromToken(token));
            context.setOrganizationId(jwtUtils.getOrganizationIdFromToken(token));
            UserContext.set(context);

            return joinPoint.proceed();
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 从请求头中提取token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new UnauthorizedException("Missing or invalid Authorization header");
    }
} 