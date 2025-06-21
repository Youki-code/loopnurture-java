package org.springframework.samples.loopnurture.users.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = {
    "org.springframework.samples.loopnurture.users.service",
    "org.springframework.samples.loopnurture.users.infra.converter"
})
public class TestConfig {
    @MockBean
    private DiscoveryClient discoveryClient;

    // Removed passwordEncoder bean to avoid duplication with SecurityConfig during tests

    // Removed repository mock beans to avoid duplication with @MockBean declarations
    // in individual tests, which would otherwise cause ApplicationContext startup
    // errors due to multiple beans of the same type.
} 