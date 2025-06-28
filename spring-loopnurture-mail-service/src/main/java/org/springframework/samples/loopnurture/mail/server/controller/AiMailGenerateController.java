package org.springframework.samples.loopnurture.mail.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.AiMailGenerateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.AiMailGenerateResponse;
import org.springframework.samples.loopnurture.mail.service.MailTemplateGenerateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器: 调用 AI 生成营销邮件（主题 + HTML）。
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ai/mail")
@RequiredArgsConstructor
@RequireLogin
public class AiMailGenerateController {

    private final MailTemplateGenerateService mailTemplateGenerateService;

    @PostMapping("/generate")
    public ApiResponse<AiMailGenerateResponse> generate(@Valid @RequestBody AiMailGenerateRequest req) {
        long start = System.currentTimeMillis();
        log.info("[AI-Mail] generate called: {}", req);

        var result = mailTemplateGenerateService.generateEmailTemplate(
                req.getCompanyName(),
                req.getEmailPurpose(),
                req.getRequirement());

        long cost = System.currentTimeMillis() - start;
        log.info("[AI-Mail] generate finished in {} ms", cost);

        AiMailGenerateResponse resp = AiMailGenerateResponse.builder()
                .subject(result.subject())
                .html(result.html())
                .build();
        return ApiResponse.ok(resp);
    }
} 