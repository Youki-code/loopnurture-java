package org.springframework.samples.loopnurture.mail.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.exception.UnauthorizedException;
import org.springframework.samples.loopnurture.mail.server.feign.UserServiceClient;
import org.springframework.samples.loopnurture.mail.server.feign.dto.ValidateTokenRequest;
import org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse;
import org.springframework.util.StringUtils;

@Aspect
@Component
@RequiredArgsConstructor
public class RequireLoginAspect {

    private final HttpServletRequest request;
    private final UserServiceClient userServiceClient;

    @Around("@annotation(org.springframework.samples.loopnurture.mail.annotation.RequireLogin) || @within(org.springframework.samples.loopnurture.mail.annotation.RequireLogin)")
    public Object bindAndClearUserContext(ProceedingJoinPoint pjp) throws Throwable {
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException("缺少 Authorization 头，请先登录");
        }

        // 调用 Users-Service 校验 token
        TokenValidationResponse resp = userServiceClient.validateToken(new ValidateTokenRequest(token));
        if (resp == null || !resp.isValid()) {
            throw new UnauthorizedException("无效或已过期的 Token");
        }

        // 将校验结果写入 UserContext，供后续业务代码使用
        UserContext ctx = new UserContext();
        ctx.setToken(token);
        ctx.setUserId(resp.getUserId());
        ctx.setOrgCode(resp.getOrgCode());

        UserContext.set(ctx);
        try {
            return pjp.proceed();
        } finally {
            UserContext.clear();
        }
    }
} 