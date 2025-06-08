package org.springframework.samples.loopnurture.mail.server.config.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.samples.loopnurture.mail.server.feign.UserServiceClient;
import org.springframework.samples.loopnurture.mail.server.context.UserContext;
import org.springframework.samples.loopnurture.mail.server.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户认证切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserAuthAspect {

    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    /**
     * 拦截所有控制器方法，进行用户认证
     */
    @Around("execution(* org.springframework.samples.loopnurture.mail.server.controller..*.*(..))")
    public Object authenticate(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("Missing authorization token");
        }

        try {
            var response = userServiceClient.validateToken(token);
            if (!response.isValid()) {
                throw new UnauthorizedException("Invalid token");
            }

            // 设置用户上下文
            userContext.setUserId(response.getUserId());
            userContext.setOrgId(response.getOrgId());
            userContext.setUsername(response.getUsername());

            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("Failed to authenticate user", e);
            throw new UnauthorizedException("Authentication failed");
        } finally {
            // 清理用户上下文
            userContext.clear();
        }
    }
} 