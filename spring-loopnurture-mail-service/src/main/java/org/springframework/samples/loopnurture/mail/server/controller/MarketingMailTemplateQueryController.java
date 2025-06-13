package org.springframework.samples.loopnurture.mail.server.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateQueryService;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ModifyMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplateResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplatePageRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.TemplateIdRequest;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.context.UserContext;

import jakarta.validation.Valid;

/**
 * 营销邮件模板控制器
 */
@RestController
@RequestMapping("/api/v1/marketing/template")
@RequiredArgsConstructor
@RequireLogin
public class MarketingMailTemplateQueryController {

    private final EmailTemplateQueryService templateQueryService;
    private final MarketingEmailTemplateConverter templateConverter;

    /**
     * 获取营销邮件模板详情
     */
    @RequireLogin
    @PostMapping("/getTemplateDetail")
    public ApiResponse<MarketingEmailTemplateResponse> getTemplateDetail(@RequestBody TemplateIdRequest request) {
        String templateId = request.getTemplateId();
        MarketingEmailTemplateDO template = templateQueryService.getTemplateById(templateId);
        return ApiResponse.ok(templateConverter.toResponse(template));
    }

    /**
     * 分页查询营销邮件模板列表
     */
    @RequireLogin
    @PostMapping("/pageTemplates")
    public ApiResponse<Page<MarketingEmailTemplateResponse>> pageTemplates(
            @Valid @RequestBody MarketingEmailTemplatePageRequest request) {
        Page<MarketingEmailTemplateDO> templates = templateQueryService.pageTemplates(
            request.getOrgCode(),
            request.getTemplateId(),
            request.getTemplateName(),
            request.getContentType(),
            request.getEnableStatusList(),
            request.toPageable()
        );
        return ApiResponse.ok(templates.map(templateConverter::toResponse));
    }
} 