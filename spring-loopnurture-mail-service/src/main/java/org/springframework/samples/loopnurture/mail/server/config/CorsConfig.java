package org.springframework.samples.loopnurture.mail.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许任意 Origin（Origin 会在响应中回显具体值）
                .allowedOriginPatterns("*")
                // 支持常见方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                // 允许携带 Cookie/Authorization
                .allowCredentials(true)
                // 预检缓存 1 小时
                .maxAge(3600);
    }
} 