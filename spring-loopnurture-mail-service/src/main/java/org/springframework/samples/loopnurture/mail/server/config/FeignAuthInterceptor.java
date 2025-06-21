package org.springframework.samples.loopnurture.mail.server.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.mail.context.UserContext;

/**
 * 在调用其他服务时，将当前请求的 Authorization 头转发出去
 */
@Component
public class FeignAuthInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String token = UserContext.get() != null ? UserContext.get().getToken() : null;
        if (StringUtils.hasText(token)) {
            template.header("Authorization", token);
        }
    }
} 