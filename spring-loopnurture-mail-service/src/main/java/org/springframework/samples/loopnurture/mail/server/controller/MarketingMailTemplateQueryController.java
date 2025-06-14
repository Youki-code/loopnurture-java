package org.springframework.samples.loopnurture.mail.server.controller;

import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateQueryService;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.dto.MarketingEmailTemplatePageQueryDTO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplateResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplatePageRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.TemplateIdRequest;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

/**
 * 营销邮件模板控制器
 */
@RestController
@RequestMapping("/api/v1/marketing/template")
@RequiredArgsConstructor
@RequireLogin
public class MarketingMailTemplateQueryController {


    private final MarketingEmailTemplateConverter templateConverter;

    private final MarketingEmailTemplateRepository marketingEmailTemplateRepository;

    /**
     * 获取营销邮件模板详情
     */
    @RequireLogin
    @PostMapping("/getTemplateDetail")
    public ApiResponse<MarketingEmailTemplateResponse> getTemplateDetail(@RequestBody TemplateIdRequest request) {
        String templateId = request.getTemplateId();
        MarketingEmailTemplateDO template = marketingEmailTemplateRepository.getByTemplateId(templateId);
        if(template==null){
            throw new ValidationException("Template not found");
        }
        return ApiResponse.ok(templateConverter.toResponse(template));
    }

    /**
     * 分页查询营销邮件模板列表
     */
    @RequireLogin
    @PostMapping("/pageTemplates")
    public ApiResponse<Page<MarketingEmailTemplateResponse>> pageTemplates(
            @Valid @RequestBody MarketingEmailTemplatePageRequest request) {
        Page<MarketingEmailTemplateDO> templates = marketingEmailTemplateRepository.pageQuery(
            MarketingEmailTemplatePageQueryDTO.builder()
                .orgCode(request.getOrgCode())
                .templateId(request.getTemplateId())
                .templateName(request.getTemplateName())
                .contentType(request.getContentType())
                .enableStatusList(request.getEnableStatusList())
                .build()
        );
        return ApiResponse.ok(templates.map(templateConverter::toResponse));
    }
} 