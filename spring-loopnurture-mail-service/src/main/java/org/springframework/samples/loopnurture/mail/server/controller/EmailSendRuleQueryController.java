package org.springframework.samples.loopnurture.mail.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.service.EmailSendRuleQueryService;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.DetailEmailSendRuleRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.EmailSendRuleDetailResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.EmailSendRulePageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/email/rules")
@RequiredArgsConstructor
public class EmailSendRuleQueryController {

    private final EmailSendRuleQueryService emailSendRuleService;
    private final UserContext userContext;


    @RequireLogin
    @PostMapping("/getRuleDetail")
    public ApiResponse<EmailSendRuleDetailResponse> getRuleDetail(@RequestBody DetailEmailSendRuleRequest request) {
        String ruleId = request.getRuleId();
        EmailSendRuleDO rule = emailSendRuleService.getRule(ruleId);
        EmailSendRuleDetailResponse response = EmailSendRuleDetailResponse.builder()
                .ruleId(rule.getRuleId())
                .ruleName(rule.getRuleName())
                .templateId(rule.getTemplateId())
                .ruleType(rule.getRuleType())
                .cronExpression(rule.getCronExpression())
                .fixedRate(rule.getFixedRate())
                .fixedDelay(rule.getFixedDelay())
                .recipients(rule.getRecipients())
                .cc(rule.getCc())
                .bcc(rule.getBcc())
                .startTime(rule.getStartTime())
                .endTime(rule.getEndTime())
                .maxExecutions(rule.getMaxExecutions())
                .executionCount(rule.getExecutionCount())
                .lastExecutionTime(rule.getLastExecutionTime())
                .nextExecutionTime(rule.getNextExecutionTime())
                .isActive(rule.getIsActive())
                .createdAt(rule.getCreatedAt())
                .updatedAt(rule.getUpdatedAt())
                .createdBy(rule.getCreatedBy())
                .updatedBy(rule.getUpdatedBy())
                .build();
        return ApiResponse.ok(response);
    }

    @RequireLogin
    @PostMapping("/pageRules")
    public ApiResponse<Page<EmailSendRuleDetailResponse>> pageRules(
            @Valid @RequestBody EmailSendRulePageRequest request) {
        Page<EmailSendRuleDO> rules = emailSendRuleService.getRules(
                userContext.getOrgCode(),
                PageRequest.of(request.getPageNum(), request.getPageSize())
        );

        Page<EmailSendRuleDetailResponse> response = rules.map(rule ->
                EmailSendRuleDetailResponse.builder()
                        .ruleId(rule.getRuleId())
                        .ruleName(rule.getRuleName())
                        .templateId(rule.getTemplateId())
                        .ruleType(rule.getRuleType())
                        .cronExpression(rule.getCronExpression())
                        .fixedRate(rule.getFixedRate())
                        .fixedDelay(rule.getFixedDelay())
                        .recipients(rule.getRecipients())
                        .cc(rule.getCc())
                        .bcc(rule.getBcc())
                        .startTime(rule.getStartTime())
                        .endTime(rule.getEndTime())
                        .maxExecutions(rule.getMaxExecutions())
                        .executionCount(rule.getExecutionCount())
                        .lastExecutionTime(rule.getLastExecutionTime())
                        .nextExecutionTime(rule.getNextExecutionTime())
                        .isActive(rule.getIsActive())
                        .createdAt(rule.getCreatedAt())
                        .updatedAt(rule.getUpdatedAt())
                        .createdBy(rule.getCreatedBy())
                        .updatedBy(rule.getUpdatedBy())
                        .build()
        );
        return ApiResponse.ok(response);
    }
} 