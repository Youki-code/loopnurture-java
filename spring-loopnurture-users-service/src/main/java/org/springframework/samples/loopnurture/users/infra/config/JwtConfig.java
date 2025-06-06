package org.springframework.samples.loopnurture.users.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * JWT配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    /**
     * JWT密钥
     */
    private String secret = "loopnurture_default_jwt_secret_key_please_change_in_production";
    
    /**
     * token有效期（秒）
     */
    private long expiration = 86400; // 24小时
} 