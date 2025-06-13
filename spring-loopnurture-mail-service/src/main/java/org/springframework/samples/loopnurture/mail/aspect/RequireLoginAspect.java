package org.springframework.samples.loopnurture.mail.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.exception.UnauthorizedException;

@Aspect
@Component
@RequiredArgsConstructor
public class RequireLoginAspect {
    /**
     * 伪代码：从安全上下文或请求头中解析用户信息。
     * 这里只演示 ThreadLocal 生命周期管理。
     */
    @Around("@annotation(org.springframework.samples.loopnurture.mail.annotation.RequireLogin)")
    public Object bindAndClearUserContext(ProceedingJoinPoint pjp) throws Throwable {
        // TODO 从实际认证信息构建 UserContext
        UserContext ctx = new UserContext();
        ctx.setUserId("demoUser");
        ctx.setOrgCode("demoOrg");

        UserContext.set(ctx);
        try {
            if (ctx.getUserId() == null) {
                throw new UnauthorizedException("用户未登录");
            }
            return pjp.proceed();
        } finally {
            UserContext.clear();
        }
    }
} 