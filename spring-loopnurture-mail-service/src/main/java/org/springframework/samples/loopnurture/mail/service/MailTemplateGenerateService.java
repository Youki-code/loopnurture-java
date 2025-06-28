package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

// Qiniu SDK imports
import com.qiniu.util.Auth;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.common.QiniuException;

import java.util.HashMap;
import java.util.Map;

import org.springframework.samples.loopnurture.mail.domain.model.vo.AiStrategyContentVO;

/**
 * Service responsible for generating marketing email templates with the help of an external LLM (Claude Sonnet-4).
 * <p>
 * Usage example:
 * <pre>
 * String emailPlanJson = mailTemplateGenerateService.generateIntentText("test", "Promotion",
 *         "Help me generate an email promoting the restaurant");
 * </pre>
 * The returned string is the raw response body from the Claude API which already follows the structure
 * specified by the {@code system} prompt (subject line variants, preview text, body structure, …).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailTemplateGenerateService {

    /**
     * Claude model version to call. Kept as constant because it rarely changes and is mandated by the business spec.
     */
    private static final String MODEL_NAME = "claude-sonnet-4-20250514";

    // 系统级 prompt 将从 AiStrategyContentVO 中动态读取，不再使用类常量。

    private final RestTemplate restTemplate = new RestTemplate();

    private final AiStrategyQueryService aiStrategyQueryService;

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Value("${anthropic.endpoint-url:https://api.anthropic.com/v1/messages}")
    private String endpointUrl;

    @Value("${anthropic.api-key:}")
    private String apiKey;

    // ================= WORKFLOW CONSTANTS =================

    // ================= OPENAI / DALL·E Configuration =================

    @Value("${openai.endpoint-url:https://api.openai.com/v1/images/generations}")
    private String openAiEndpointUrl;

    @Value("${openai.api-key:}")
    private String openAiApiKey;

    // ================= Qiniu Configuration =================

    @Value("${qiniu.access-key:}")
    private String qiniuAccessKey;

    @Value("${qiniu.secret-key:}")
    private String qiniuSecretKey;

    @Value("${qiniu.bucket:}")
    private String qiniuBucket;

    /** 公网访问域名，如 https://cdn.yourdomain.com */
    @Value("${qiniu.bucket-domain:}")
    private String qiniuDomain;

    // ================================================================

    /**
     * Generates a structured email content plan for the specified campaign parameters.
     *
     * @param companyName  the brand / company name (mandatory)
     * @param emailPurpose the high-level purpose of the email, e.g. Promotion, On-Boarding… (mandatory)
     * @param requirement  free-form description of the scenario / requirements from the business user (mandatory)
     * @return The raw response body returned by the Claude API (a JSON string).
     * @throws IllegalArgumentException if any of the required parameters are blank
     * @throws RuntimeException         if the Claude API call fails or returns a non-2xx status code
     */
    public String generateIntentText(String companyName, String emailPurpose, String requirement) {
        AiStrategyContentVO contentVO = loadPrompts();
        if (contentVO == null || !StringUtils.hasText(contentVO.getIntentPrompt())) {
            throw new IllegalStateException("Intent prompt not configured in AI strategy");
        }
        String systemPromptToUse = contentVO.getIntentPrompt();

        // Basic argument guards (fail-fast before spending tokens)
        if (!StringUtils.hasText(companyName) || !StringUtils.hasText(emailPurpose) || !StringUtils.hasText(requirement)) {
            throw new IllegalArgumentException("companyName, emailPurpose and requirement must all be provided and non-blank");
        }

        String userPrompt = buildUserPrompt(companyName, emailPurpose, requirement);
        log.info("[MailTpl] Intent generation - userPrompt={}", userPrompt.replaceAll("\n", " | "));

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", MODEL_NAME);
        payload.put("max_tokens", 4000);
        payload.put("temperature", 1);
        payload.put("system", systemPromptToUse);
        payload.put("prompt", userPrompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Anthropics uses "x-api-key" header. We only add it when configured.
        if (StringUtils.hasText(apiKey)) {
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");
        }

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
        if (log.isDebugEnabled()) {
            try {
                log.debug("[MailTpl] Claude request payload={}", objectMapper.writeValueAsString(payload));
            } catch (com.fasterxml.jackson.core.JsonProcessingException ignore) {}
        }

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(endpointUrl, requestEntity, String.class);
            log.info("[MailTpl] Claude HTTP {} ({} chars)", response.getStatusCode(), response.hasBody() ? response.getBody().length() : 0);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Claude API returned non-success status: {}. body={}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Failed to generate intent text. Claude API status=" + response.getStatusCode());
            }
            log.info("[MailTpl] Intent generation completed");
            return response.getBody();
        } catch (RestClientException ex) {
            log.error("Error calling Claude API endpointUrl:{}", endpointUrl, ex);
            throw new RuntimeException("Error calling Claude API", ex);
        }
    }

    private String buildUserPrompt(String companyName, String emailPurpose, String requirement) {
        return "Company Name: " + companyName + "\n" +
               "Email Purpose: " + emailPurpose + "\n" +
               "Scenario Description: " + requirement;
    }

    /**
     * Produce the final HTML template (with embedded image) along with the subject line.
     */
    public EmailGenerationResult generateEmailTemplate(String companyName, String emailPurpose, String requirement) {
        AiStrategyContentVO contentVO = loadPrompts();
        if (contentVO == null ||
                !StringUtils.hasText(contentVO.getIntentPrompt()) ||
                !StringUtils.hasText(contentVO.getEmailHtmlPrompt())) {
            throw new IllegalStateException("AI strategy prompts not configured");
        }
        String intentSystemPrompt = contentVO.getIntentPrompt();
        String emailSystemPrompt = contentVO.getEmailHtmlPrompt();

        // 1) Build user prompt
        String userPrompt = buildUserPrompt(companyName, emailPurpose, requirement);
        log.info("[MailTpl] Email generation start - userPrompt={}", userPrompt.replaceAll("\n", " | "));

        // 2) Intent analysis via Claude
        String intentText = callClaude(intentSystemPrompt, userPrompt);
        log.info("[MailTpl] IntentText received: {}", intentText.length() > 200 ? intentText.substring(0, 200) + "..." : intentText);

        // 3) HTML email generation via Claude (include previous context as JSON string)
        String composerPrompt = java.util.Map.of(
                "originalInput", userPrompt,
                "intentAnalysis", intentText
        ).toString();

        String emailComposerRaw = callClaude(emailSystemPrompt, composerPrompt);
        log.info("[MailTpl] Email composer raw length={} chars", emailComposerRaw.length());

        // 4) Separate subject line & HTML, and detect image placeholder
        String subject = extractSubjectLine(emailComposerRaw);
        String html = extractHtml(emailComposerRaw);

        // 5) Handle image placeholder if present
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\[Image: (.*?)\\]").matcher(html);
        if (matcher.find()) {
            String imageDescription = matcher.group(1);
            log.info("[MailTpl] Image placeholder detected: {}", imageDescription);
            String imageUrl = generateImage(imageDescription);
            log.info("[MailTpl] Image generated and uploaded url={}", imageUrl);
            html = html.replaceFirst("\\[Image: .*?\\]", "<img src=\"" + imageUrl + "\" alt=\"" + imageDescription.replaceAll("\"", "&quot;") + "\" style=\"max-width:100%;\"/>");
        }

        log.info("[MailTpl] Email generation done - subject={}, htmlLength={} chars", subject, html.length());

        return new EmailGenerationResult(subject, html);
    }

    // ================= Helper methods =================

    private String callClaude(String systemPrompt, String prompt) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", MODEL_NAME);
        payload.put("max_tokens", 4000);
        payload.put("temperature", 1);
        payload.put("system", systemPrompt);
        // Anthropic v1 requires messages array
        java.util.List<Map<String, String>> messages = java.util.List.of(
                java.util.Map.of("role", "user", "content", prompt)
        );
        payload.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasText(apiKey)) {
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");
        }

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        if (log.isDebugEnabled()) {
            try {
                log.debug("[MailTpl] Claude request payload={}", objectMapper.writeValueAsString(payload));
            } catch (com.fasterxml.jackson.core.JsonProcessingException ignore) {}
        }

        try {
            ResponseEntity<String> resp = restTemplate.postForEntity(endpointUrl, entity, String.class);
            log.info("[MailTpl] Claude HTTP {} ({} chars)", resp.getStatusCode(), resp.hasBody() ? resp.getBody().length() : 0);

            if (!resp.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Claude API error status=" + resp.getStatusCode());
            }
            return resp.getBody();
        } catch (RestClientException ex) {
            log.error("Error calling Claude API endpointUrl:{}", endpointUrl, ex);
            throw new RuntimeException("Claude API invocation failed", ex);
        }
    }

    private String generateImage(String prompt) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "dall-e-3");
        payload.put("prompt", prompt);
        payload.put("style", "natural");
        payload.put("quality", "hd");
        payload.put("response_format", "url");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!StringUtils.hasText(openAiApiKey)) {
            throw new IllegalStateException("openai.api-key not configured");
        }
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        if (log.isDebugEnabled()) {
            try {
                log.debug("[MailTpl] OpenAI image request payload={}", objectMapper.writeValueAsString(payload));
            } catch (com.fasterxml.jackson.core.JsonProcessingException ignore) {}
        }

        try {
            ResponseEntity<Map> resp = restTemplate.postForEntity(openAiEndpointUrl, entity, Map.class);
            log.info("[MailTpl] OpenAI HTTP {}", resp.getStatusCode());

            if (!resp.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("OpenAI image generation error status=" + resp.getStatusCode());
            }
            Object urlObj = ((java.util.List<?>) ((Map<?, ?>) resp.getBody()).get("data")).get(0);
            if (urlObj instanceof Map<?, ?> urls) {
                String openAiUrl = (String) urls.get("url");
                return uploadToQiniu(openAiUrl);
            }
            throw new RuntimeException("Unexpected OpenAI response structure");
        } catch (RestClientException ex) {
            throw new RuntimeException("OpenAI API invocation failed", ex);
        }
    }

    private String extractSubjectLine(String composerRaw) {
        for (String line : composerRaw.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.toLowerCase().startsWith("subject:")) {
                return trimmed.replaceFirst("(?i)subject:\\s*", "").trim();
            }
            if (!trimmed.isEmpty() && !trimmed.startsWith("<")) {
                return trimmed;
            }
        }
        return "";
    }

    private String extractHtml(String composerRaw) {
        int idx = composerRaw.indexOf("<");
        if (idx >= 0) {
            return composerRaw.substring(idx);
        }
        return composerRaw;
    }

    /**
     * 使用七牛云的 fetch 接口将远程图片转存到指定 Bucket，并返回可公网访问的 URL。
     */
    private String uploadToQiniu(String remoteImageUrl) {
        if (!org.springframework.util.StringUtils.hasText(qiniuAccessKey) ||
            !org.springframework.util.StringUtils.hasText(qiniuSecretKey) ||
            !org.springframework.util.StringUtils.hasText(qiniuBucket) ||
            !org.springframework.util.StringUtils.hasText(qiniuDomain)) {
            // 如果没有配置七牛信息，直接返回原始 URL
            log.warn("Qiniu config not set, fallback to original image URL.");
            return remoteImageUrl;
        }

        try {
            Auth auth = Auth.create(qiniuAccessKey, qiniuSecretKey);
            Configuration cfg = new Configuration(Region.autoRegion());
            BucketManager bucketManager = new BucketManager(auth, cfg);

            String key = "ai/" + java.util.UUID.randomUUID() + ".png";
            FetchRet ret = bucketManager.fetch(remoteImageUrl, qiniuBucket, key);
            // 返回自定义域名的访问 URL
            String url = qiniuDomain.endsWith("/") ? qiniuDomain + key : qiniuDomain + "/" + key;
            log.info("[MailTpl] Image uploaded to Qiniu key={} -> url={}", key, url);
            return url;
        } catch (QiniuException ex) {
            log.error("Upload image to Qiniu failed", ex);
            return remoteImageUrl; // 回退到原始 URL
        }
    }

    /**
     * Load latest enabled prompts from AI strategy repository.
     */
    private AiStrategyContentVO loadPrompts() {
        try {
            var strategy = aiStrategyQueryService.getLatestEnabledStrategy(
                    org.springframework.samples.loopnurture.mail.domain.enums.AiStrategyTypeEnum.MARKETING_MAIL.getCode());
            if (strategy == null || !StringUtils.hasText(strategy.getAiStrategyContent())) {
                return null;
            }
            return objectMapper.readValue(strategy.getAiStrategyContent(),
                    org.springframework.samples.loopnurture.mail.domain.model.vo.AiStrategyContentVO.class);
        } catch (Exception e) {
            log.warn("Failed to load AI prompts from strategy repository, fallback to defaults", e);
            return null;
        }
    }

    /**
     * Simple container for the final output sent to front-end.
     */
    public record EmailGenerationResult(String subject, String html) {}

} 