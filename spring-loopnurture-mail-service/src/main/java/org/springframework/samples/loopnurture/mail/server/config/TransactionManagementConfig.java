package org.springframework.samples.loopnurture.mail.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 事务管理配置类
 * 启用@Transactional注解支持
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagementConfig {
    // 启用声明式事务管理
    // @EnableTransactionManagement 注解会启用 @Transactional 注解的处理
} 