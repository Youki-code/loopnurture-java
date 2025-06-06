package org.springframework.samples.loopnurture.mail.server.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.samples.loopnurture.mail.server.context.UserContext;
import org.springframework.samples.loopnurture.mail.server.exception.UnauthorizedException;
import org.springframework.samples.loopnurture.mail.server.feign.UserServiceClient;
import org.springframework.samples.loopnurture.mail.server.annotation.RequireLogin;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户认证切面
 */
@Slf4j
@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class UserAuthAspect {

    private final UserServiceClient userServiceClient;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Around("@annotation(requireLogin)")
    public Object checkAuth(ProceedingJoinPoint joinPoint, RequireLogin requireLogin) throws Throwable {
        try {
            // 获取请求头中的token
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authHeader = request.getHeader(AUTH_HEADER);

            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                throw new UnauthorizedException("未登录或token已过期");
            }

            String token = authHeader.substring(BEARER_PREFIX.length());
            
            try {
                // 调用用户服务验证token并获取用户信息
                var userInfo = userServiceClient.validateToken(token);
                
                // 设置用户上下文
                UserContext userContext = new UserContext();
                userContext.setUserId(userInfo.getUserId());
                userContext.setUsername(userInfo.getUsername());
                userContext.setOrgId(userInfo.getOrgId());
                userContext.setOrgName(userInfo.getOrgName());
                UserContext.setUser(userContext);

                // 执行实际的业务方法
                return joinPoint.proceed();
            } catch (Exception e) {
                log.error("Failed to validate token: {}", e.getMessage());
                throw new UnauthorizedException("token验证失败");
            }
        } finally {
            // 清理ThreadLocal
            UserContext.clear();
        }
    }
} 