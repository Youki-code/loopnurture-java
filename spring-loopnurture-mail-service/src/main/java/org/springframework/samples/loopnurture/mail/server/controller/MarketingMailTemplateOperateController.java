package org.springframework.samples.loopnurture.mail.server.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplatePageRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplateResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ModifyMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.TemplateIdRequest;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateOperateService;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateQueryService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 营销邮件模板控制器
 */
@RestController
@RequestMapping("/api/v1/marketing/template")
@RequiredArgsConstructor
@RequireLogin
public class MarketingMailTemplateOperateController {

    private final EmailTemplateQueryService templateQueryService;
    private final EmailTemplateOperateService templateOperateService;
    private final MarketingEmailTemplateConverter templateConverter;

    /**
     * 创建营销邮件模板
     */
    @RequireLogin
    @PostMapping("/createTemplate")
    public ApiResponse<MarketingEmailTemplateResponse> createTemplate(
            @Valid @RequestBody CreateMarketingEmailTemplateRequest request) {
        // 判断templateName是否已存在
        List<String> dup = templateQueryService.queryByTemplateName(UserContext.getOrgCode(), request.getTemplateName());
        if (!CollectionUtils.isEmpty(dup)) {
            return ApiResponse.error(400, "Template name already exists");
        }
        MarketingEmailTemplateDO template = templateOperateService.createTemplate(request);
        return ApiResponse.ok(templateConverter.toResponse(template));
    }

    /**
     * 修改营销邮件模板
     */
    @RequireLogin
    @PostMapping("/modifyTemplate")
    public ApiResponse<MarketingEmailTemplateResponse> modifyTemplate(
            @Valid @RequestBody ModifyMarketingEmailTemplateRequest request) {
        MarketingEmailTemplateDO template = templateOperateService.updateTemplate(request);
        return ApiResponse.ok(templateConverter.toResponse(template));
    }

    /**
     * 删除营销邮件模板
     */
    @RequireLogin
    @PostMapping("/deleteTemplate")
    public ApiResponse<Void> deleteTemplate(@RequestBody TemplateIdRequest request) {
        String templateId = request.getTemplateId();
        templateOperateService.deleteTemplate(templateId);
        return ApiResponse.ok(null);
    }


} 