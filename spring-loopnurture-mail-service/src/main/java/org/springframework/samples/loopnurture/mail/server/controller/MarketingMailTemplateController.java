package org.springframework.samples.loopnurture.mail.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateService;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ModifyMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplateResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplatePageRequest;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;

import jakarta.validation.Valid;

/**
 * 营销邮件模板控制器
 */
@RestController
@RequestMapping("/api/v1/marketing/template")
@RequiredArgsConstructor
@RequireLogin
public class MarketingMailTemplateController {

    private final EmailTemplateService templateService;
    private final MarketingEmailTemplateConverter templateConverter;

    /**
     * 创建营销邮件模板
     */
    @PostMapping("/createTemplate")
    public ResponseEntity<MarketingEmailTemplateResponse> createTemplate(
            @Valid @RequestBody CreateMarketingEmailTemplateRequest request) {
        MarketingEmailTemplateDO template = templateConverter.toEntity(request);
        template = templateService.createTemplate(template);
        return ResponseEntity.ok(templateConverter.toResponse(template));
    }

    /**
     * 修改营销邮件模板
     */
    @PostMapping("/modifyTemplate")
    public ResponseEntity<MarketingEmailTemplateResponse> modifyTemplate(
            @Valid @RequestBody ModifyMarketingEmailTemplateRequest request) {
        MarketingEmailTemplateDO template = templateConverter.toEntity(request);
        template = templateService.modifyTemplate(template);
        return ResponseEntity.ok(templateConverter.toResponse(template));
    }

    /**
     * 删除营销邮件模板
     */
    @PostMapping("/deleteTemplate")
    public ResponseEntity<Void> deleteTemplate(@RequestParam String templateId) {
        templateService.deleteTemplate(templateId);
        return ResponseEntity.ok().build();
    }

    /**
     * 禁用营销邮件模板
     */
    @PostMapping("/disableTemplate")
    public ResponseEntity<MarketingEmailTemplateResponse> disableTemplate(@RequestParam String templateId) {
        MarketingEmailTemplateDO template = templateService.disableTemplate(templateId);
        return ResponseEntity.ok(templateConverter.toResponse(template));
    }

    /**
     * 获取营销邮件模板详情
     */
    @PostMapping("/getTemplateDetail")
    public ResponseEntity<MarketingEmailTemplateResponse> getTemplateDetail(@RequestParam String templateId) {
        MarketingEmailTemplateDO template = templateService.getTemplateById(templateId);
        return ResponseEntity.ok(templateConverter.toResponse(template));
    }

    /**
     * 分页查询营销邮件模板列表
     */
    @PostMapping("/pageTemplates")
    public ResponseEntity<Page<MarketingEmailTemplateResponse>> pageTemplates(
            @Valid @RequestBody MarketingEmailTemplatePageRequest request) {
        Page<MarketingEmailTemplateDO> templates = templateService.pageTemplates(
            request.getOrgCode(),
            request.getTemplateId(),
            request.getTemplateName(),
            request.getContentType(),
            request.getEnableStatusList(),
            request.toPageable()
        );
        return ResponseEntity.ok(templates.map(templateConverter::toResponse));
    }
} 