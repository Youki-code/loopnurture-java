package org.springframework.samples.loopnurture.users.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
    private String secret;
    
    /**
     * token有效期（秒）
     */
    private Long expiration;
} 