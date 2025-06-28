package org.springframework.samples.loopnurture.mail.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test that calls real external LLM & Qiniu services.
 * <p>
 * This test will only run when the mandatory environment variables are set
 * (ANTHROPIC_API_KEY, OPENAI_API_KEY, QINIU_AK, QINIU_SK, QINIU_BUCKET, QINIU_BUCKET_DOMAIN).
 * Use it manually to verify end-to-end functionality.
 */
@SpringBootTest
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "ANTHROPIC_API_KEY", matches = ".+", disabledReason = "Anthropic key not found")
public class MailTemplateGenerateServiceIntegrationTest {

    @Autowired
    private MailTemplateGenerateService service;

    @Test
    void generateEmailTemplate_realLLM_shouldReturnHtml() {
        var result = service.generateEmailTemplate("LoopNurture", "Promotion", "Help me generate an email promoting the restaurant");
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.subject() != null && !result.subject().isBlank());
        Assertions.assertTrue(result.html() != null && result.html().toLowerCase().contains("<html"), "HTML should contain <html>");
    }
} 