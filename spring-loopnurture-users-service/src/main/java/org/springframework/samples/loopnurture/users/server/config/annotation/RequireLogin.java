package org.springframework.samples.loopnurture.users.server.config.annotation;

import java.lang.annotation.*;

/**
 * 需要登录注解
 * 标记在Controller方法上，表示该接口需要登录才能访问
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLogin {
} 