package org.springframework.samples.loopnurture.mail.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.client.UserServiceClient;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.exception.UnauthorizedException;

@Aspect
@Component
@RequiredArgsConstructor
public class RequireLoginAspect {

    private final UserServiceClient userServiceClient;
    private final UserContext userContext;

    @Around("@annotation(org.springframework.samples.loopnurture.mail.annotation.RequireLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = userContext.getToken();
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException("No token provided");
        }

        // 调用用户服务验证token
        boolean isValid = userServiceClient.validateToken(token);
        if (!isValid) {
            throw new UnauthorizedException("Invalid token");
        }

        return joinPoint.proceed();
    }
} 