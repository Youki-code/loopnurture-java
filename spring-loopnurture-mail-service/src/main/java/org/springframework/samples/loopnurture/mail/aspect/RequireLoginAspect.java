package org.springframework.samples.loopnurture.mail.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.exception.UnauthorizedException;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.feign.UserServiceClient;
import org.springframework.samples.loopnurture.mail.server.feign.dto.ValidateTokenRequest;
import org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
@RequiredArgsConstructor
public class RequireLoginAspect {

    private static final Logger log = LoggerFactory.getLogger(RequireLoginAspect.class);

    private final HttpServletRequest request;
    private final UserServiceClient userServiceClient;

    @Around("@annotation(org.springframework.samples.loopnurture.mail.annotation.RequireLogin) || @within(org.springframework.samples.loopnurture.mail.annotation.RequireLogin)")
    public Object bindAndClearUserContext(ProceedingJoinPoint pjp) throws Throwable {
        String token = request.getHeader("Authorization");
        if (token != null) {
            token = token.trim(); // 去掉首尾空格，避免 header 冒号后留空格导致签名不匹配
        }
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException("缺少 Authorization 头，请先登录");
        }

        // 调用 Users-Service 校验 token
        log.info("[RequireLoginAspect] Validating token: {}", token);
        System.out.println("[RequireLoginAspect] Validating token: " + token);
        ApiResponse<TokenValidationResponse> resp = userServiceClient.validateToken(new ValidateTokenRequest(token));
        log.info("[RequireLoginAspect] Validation response: {}", resp);
        System.out.println("[RequireLoginAspect] Validation response: " + resp);
        if (resp == null || !resp.getData().isValid()) {
            throw new UnauthorizedException("无效或已过期的 Token");
        }
        TokenValidationResponse data = resp.getData();

        // 将校验结果写入 UserContext，供后续业务代码使用
        UserContext ctx = new UserContext();
        ctx.setToken(token);
        ctx.setUserId(data.getUserId());
        ctx.setOrgCode(data.getOrgCode());

        UserContext.set(ctx);
        try {
            return pjp.proceed();
        } finally {
            UserContext.clear();
        }
    }
} 