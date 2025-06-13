package org.springframework.samples.loopnurture.mail.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.service.EmailSendRuleOperateService;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateQueryService;
import org.springframework.samples.loopnurture.mail.server.controller.dto.*;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/email/rules")
@RequiredArgsConstructor
public class EmailSendRuleOperateController {
    private final EmailSendRuleOperateService emailSendRuleService;
    private final UserContext userContext;
    private final EmailTemplateQueryService emailTemplateQueryService;

    @RequireLogin
    @PostMapping("/createRule")
    public ApiResponse<CreateEmailSendRuleResponse> createRule(@Valid @RequestBody CreateEmailSendRuleRequest request) {
        // 判断templateId是否存在
        MarketingEmailTemplateDO template = emailTemplateQueryService.getByTemplateId(request.getTemplateId());
        if (template == null) {
            return ApiResponse.error(400, "Template not found");
        }
        EmailSendRuleDO savedRule = emailSendRuleService.createRule(request);
        return ApiResponse.ok(new CreateEmailSendRuleResponse(savedRule.getRuleId()));
    }

    @RequireLogin
    @PostMapping("/modifyRule")
    public ApiResponse<Void> modifyRule(@Valid @RequestBody UpdateEmailSendRuleRequest request) {
        emailSendRuleService.modifyRule(request);
        return ApiResponse.ok(null);
    }

    @RequireLogin
    @PostMapping("/deleteRule")
    public ApiResponse<Void> deleteRule(@Valid @RequestBody DeleteEmailSendRuleRequest request) {
        emailSendRuleService.deleteRule(request.getRuleId());
        return ApiResponse.ok(null);
    }

    @RequireLogin
    @PostMapping("/executeRule")
    public ApiResponse<Void> executeRule(@Valid @RequestBody ExecuteEmailSendRuleRequest request) {
        emailSendRuleService.executeRule(request.getRuleId());
        return ApiResponse.ok(null);
    }

} 