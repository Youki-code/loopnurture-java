package org.springframework.samples.loopnurture.mail.server.annotation;

import java.lang.annotation.*;

/**
 * 需要登录注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLogin {
} 