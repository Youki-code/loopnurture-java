package org.springframework.samples.loopnurture.users.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.loopnurture.users.infra.utils.SnowflakeIdGenerator;

/**
 * 雪花算法配置类
 */
@Configuration
public class SnowflakeConfig {

    /**
     * 创建雪花算法ID生成器
     * 使用默认的workerId=1和datacenterId=1
     */
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator(1L, 1L);
    }
} 