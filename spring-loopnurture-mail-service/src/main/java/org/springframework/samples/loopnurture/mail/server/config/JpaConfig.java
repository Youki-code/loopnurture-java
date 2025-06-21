package org.springframework.samples.loopnurture.mail.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA配置类
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "org.springframework.samples.loopnurture.mail.infra.mapper")
public class JpaConfig {
} 