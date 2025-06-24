package org.springframework.samples.loopnurture.mail.server.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SendGrid配置类
 */
@Configuration
public class SendGridConfig {

    /**
     * 直接从环境变量 SENDGRID_API_KEY 读取；若不存在则为空串，
     * 这样在开发/测试环境不会因为缺少密钥而导致应用启动失败。
     */
    @Value("${SENDGRID_API_KEY:}")
    private String apiKey;

    @Bean
    public SendGrid sendGrid() {
        if (apiKey == null || apiKey.isBlank()) {
            // 返回一个 dummy client，避免 NullPointerException，同时给出启动日志
            System.out.println("[SendGridConfig] WARN: SENDGRID_API_KEY is not configured, SendGrid client runs in dummy mode.");
            return new SendGrid("DUMMY_KEY");
        }
        return new SendGrid(apiKey);
    }
} 