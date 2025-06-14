package org.springframework.samples.loopnurture.mail.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.vo.EmailSendRuleExtendsInfoVO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.dto.EmailSendRulePageQueryDTO;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;
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

    private final EmailSendRuleRepository emailSendRuleRepository;


    @RequireLogin
    @PostMapping("/getRuleDetail")
    public ApiResponse<EmailSendRuleDetailResponse> getRuleDetail(@RequestBody DetailEmailSendRuleRequest request) {
        String ruleId = request.getRuleId();
        EmailSendRuleDO rule = emailSendRuleRepository.findByRuleId(ruleId);
        if(rule==null){
            throw new ResourceNotFoundException("Rule not found");
        }
        EmailSendRuleDetailResponse response = toDetailResponse(rule);
        return ApiResponse.ok(response);
    }

    @RequireLogin
    @PostMapping("/pageRules")
    public ApiResponse<Page<EmailSendRuleDetailResponse>> pageRules(
            @Valid @RequestBody EmailSendRulePageRequest request) {
                EmailSendRulePageQueryDTO query = EmailSendRulePageQueryDTO.builder()
                        .ruleName(request.getRuleName())
                        .templateId(request.getTemplateId())
                        .ruleType(request.getRuleType())
                        .recipientType(request.getRecipientType())
                        .enableStatusList(request.getEnableStatusList())
                        .orgCode(UserContext.getOrgCode())
                        .pageNum(request.getPageNum())
                        .pageSize(request.getPageSize())
                        .build();
        Page<EmailSendRuleDO> rules = emailSendRuleRepository.pageQuery(query);

        Page<EmailSendRuleDetailResponse> response = rules.map(this::toDetailResponse);
        return ApiResponse.ok(response);
    }

    /**
     * 将 EmailSendRuleDO 转换为 EmailSendRuleDetailResponse
     * @param rule
     * @return
     */
    private EmailSendRuleDetailResponse toDetailResponse(EmailSendRuleDO rule) {
        EmailSendRuleExtendsInfoVO extendsInfo = rule.getExtendsInfo();
        return EmailSendRuleDetailResponse.builder()
                .ruleId(rule.getRuleId())
                .ruleName(rule.getRuleName())
                .templateId(rule.getTemplateId())
                .ruleType(rule.getRuleType())
                .cronExpression(extendsInfo == null ? null : extendsInfo.getCronExpression())
                .fixedRate(extendsInfo == null ? null : extendsInfo.getFixedRate())
                .fixedDelay(extendsInfo == null ? null : extendsInfo.getFixedDelay())
                .recipients(extendsInfo == null ? null : extendsInfo.getRecipients())
                .cc(extendsInfo == null ? null : extendsInfo.getCc())
                .bcc(extendsInfo == null ? null : extendsInfo.getBcc())
                .startTime(rule.getStartTime())
                .endTime(rule.getEndTime())
                .maxExecutions(rule.getMaxExecutions())
                .executionCount(rule.getExecutionCount())
                .lastExecutionTime(rule.getLastExecutionTime())
                .nextExecutionTime(rule.getNextExecutionTime())
                .enableStatus(rule.getEnableStatus().getCode())
                .createdAt(rule.getCreatedAt())
                .updatedAt(rule.getUpdatedAt())
                .createdBy(rule.getCreatedBy())
                .updatedBy(rule.getUpdatedBy())
                .build();
    }
} 