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
     * 优先读取 application.yml / Config-Server 中的 sendgrid.api-key；
     * 若不存在则回退到环境变量 SENDGRID_API_KEY；再无则为空串。
     */
    @Value("${sendgrid.api-key:${SENDGRID_API_KEY:}}")
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