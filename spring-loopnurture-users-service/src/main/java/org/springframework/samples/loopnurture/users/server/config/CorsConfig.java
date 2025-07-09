package org.springframework.samples.loopnurture.users.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许任意 Origin，Spring Boot 2.4+ 建议使用 allowedOriginPatterns 以支持通配符
                .allowedOriginPatterns("*")
                // 允许常用 HTTP 方法，也可直接使用 "*"
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                // 允许所有请求头
                .allowedHeaders("*")
                // 允许携带 cookie、Authorization 等凭证信息
                .allowCredentials(true)
                // 预检请求缓存时间，单位秒
                .maxAge(3600);
    }
} 