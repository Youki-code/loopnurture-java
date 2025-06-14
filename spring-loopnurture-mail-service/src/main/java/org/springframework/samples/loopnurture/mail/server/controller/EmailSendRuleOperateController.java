package org.springframework.samples.loopnurture.mail.server.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.service.EmailSendRuleOperateService;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateEmailSendRuleRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateEmailSendRuleResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.DeleteEmailSendRuleRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ExecuteEmailSendRuleRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.UpdateEmailSendRuleRequest;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/email/rules")
@RequiredArgsConstructor
public class EmailSendRuleOperateController {
    private final EmailSendRuleOperateService emailSendRuleOperateService;

    @RequireLogin
    @PostMapping("/createRule")
    public ApiResponse<CreateEmailSendRuleResponse> createRule(@Valid @RequestBody CreateEmailSendRuleRequest request) {
        EmailSendRuleDO savedRule = emailSendRuleOperateService.createRule(request);
        return ApiResponse.ok(new CreateEmailSendRuleResponse(savedRule.getRuleId()));
    }

    @RequireLogin
    @PostMapping("/modifyRule")
    public ApiResponse<Void> modifyRule(@Valid @RequestBody UpdateEmailSendRuleRequest request) {
        emailSendRuleOperateService.modifyRule(request);
        return ApiResponse.ok(null);
    }

    @RequireLogin
    @PostMapping("/deleteRule")
    public ApiResponse<Void> deleteRule(@Valid @RequestBody DeleteEmailSendRuleRequest request) {
        emailSendRuleOperateService.deleteRule(request.getRuleId());
        return ApiResponse.ok(null);
    }

    @RequireLogin
    @PostMapping("/executeRule")
    public ApiResponse<Void> executeRule(@Valid @RequestBody ExecuteEmailSendRuleRequest request) {
        emailSendRuleOperateService.executeRule(request.getRuleId());
        return ApiResponse.ok(null);
    }

} 