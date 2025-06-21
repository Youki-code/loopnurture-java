package org.springframework.samples.loopnurture.mail.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务配置类
 * 配置手动事务管理和TransactionTemplate
 */
@Configuration
public class TransactionConfig {

    /**
     * 配置TransactionTemplate用于手动事务管理
     */
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        // 设置事务传播行为 - 使用常量而不是字符串
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 设置事务隔离级别 - 使用常量而不是字符串
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        // 设置超时时间（秒）
        template.setTimeout(30);
        return template;
    }
} 