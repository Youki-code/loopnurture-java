package org.springframework.samples.loopnurture.mail.server.config.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.exception.UnauthorizedException;
import org.aspectj.lang.JoinPoint;

/**
 * 用户认证切面
 */
@Aspect
@Component
public class UserAuthAspect {

    @Before("@annotation(org.springframework.samples.loopnurture.mail.annotation.RequireLogin)")
    public void checkAuth(JoinPoint joinPoint) {
        if (UserContext.get() == null || UserContext.get().getToken() == null) {
            throw new UnauthorizedException("用户未登录");
        }
    }
} 