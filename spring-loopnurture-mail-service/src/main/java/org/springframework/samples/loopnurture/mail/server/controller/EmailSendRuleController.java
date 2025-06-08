package org.springframework.samples.loopnurture.mail.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.mail.service.EmailSendService;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendLogDO;
import org.springframework.samples.loopnurture.mail.domain.enums.RuleTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.RecipientTypeEnum;
import org.springframework.samples.loopnurture.mail.server.dto.ModifyEmailSendRuleRequest;
import org.springframework.samples.loopnurture.mail.server.dto.CreateEmailSendRuleRequest;
import org.springframework.samples.loopnurture.mail.server.dto.EmailSendRulePageRequest;
import org.springframework.samples.loopnurture.mail.server.context.UserContext;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 邮件发送规则控制器
 */
@RestController
@RequestMapping("/api/v1/email/rules")
@RequiredArgsConstructor
@RequireLogin
public class EmailSendRuleController {

    private static final DateTimeFormatter RULE_NAME_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final EmailSendService emailService;
    private final UserContext userContext;

    /**
     * 创建邮件发送规则
     */
    @PostMapping("/createRule")
    public ResponseEntity<EmailSendRuleDO> createRule(@Valid @RequestBody CreateEmailSendRuleRequest request) {
        EmailSendRuleDO rule = convertToRule(request);
        
        // 生成规则代码
        rule.setRuleCode(generateRuleCode());
        
        // 如果规则名称为空，自动生成
        if (!StringUtils.hasText(rule.getRuleName())) {
            rule.setRuleName(generateRuleName(rule.getRuleCode()));
        }
        
        // 如果是立即发送类型，直接执行发送
        if (RuleTypeEnum.IMMEDIATE.getCode().equals(request.getRuleType())) {
            List<EmailSendLogDO> sendLogs = emailService.executeRule(rule);
            return ResponseEntity.ok(rule);
        }
        
        // 创建规则
        rule = emailService.createRule(rule);
        return ResponseEntity.ok(rule);
    }

    /**
     * 更新邮件发送规则
     */
    @PostMapping("/updateRule")
    public ResponseEntity<EmailSendRuleDO> updateRule(@Valid @RequestBody ModifyEmailSendRuleRequest request) {
        EmailSendRuleDO rule = convertToRule(request);
        rule = emailService.updateRule(rule);
        return ResponseEntity.ok(rule);
    }

    /**
     * 删除邮件发送规则
     */
    @PostMapping("/deleteRule")
    public ResponseEntity<Void> deleteRule(@RequestParam String ruleId) {
        emailService.deleteRule(ruleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取邮件发送规则详情
     */
    @PostMapping("/getRuleDetail")
    public ResponseEntity<EmailSendRuleDO> getRuleDetail(@RequestParam String ruleId) {
        return ResponseEntity.ok(emailService.getRule(ruleId));
    }

    /**
     * 分页查询邮件发送规则
     */
    @PostMapping("/pageRules")
    public ResponseEntity<Page<EmailSendRuleDO>> pageRules(
            @Valid @RequestBody EmailSendRulePageRequest request) {
        return ResponseEntity.ok(emailService.pageRules(
            request.getRuleName(),
            request.getTemplateCode(),
            request.getRuleType(),
            request.getRecipientType(),
            request.getPageable()
        ));
    }

    /**
     * 手动执行规则
     */
    @PostMapping("/executeRule")
    public ResponseEntity<List<EmailSendLogDO>> executeRule(@RequestParam String ruleId) {
        return ResponseEntity.ok(emailService.executeRule(ruleId));
    }

    private EmailSendRuleDO convertToRule(CreateEmailSendRuleRequest request) {
        EmailSendRuleDO rule = new EmailSendRuleDO();
        rule.setId(UUID.randomUUID().toString());
        rule.setOrgCode(userContext.getOrgCode());
        rule.setRuleName(request.getRuleName());
        rule.setTemplateCode(request.getTemplateCode());
        rule.setRuleType(RuleTypeEnum.fromCode(request.getRuleType()));
        rule.setCronExpression(request.getCronExpression());
        rule.setFixedRate(request.getFixedRate());
        rule.setFixedDelay(request.getFixedDelay());
        rule.setRecipients(request.getRecipients());
        rule.setCc(request.getCc());
        rule.setBcc(request.getBcc());
        rule.setStartTime(request.getStartTime());
        rule.setEndTime(request.getEndTime());
        rule.setMaxExecutions(request.getMaxExecutions());
        return rule;
    }

    private EmailSendRuleDO convertToRule(ModifyEmailSendRuleRequest request) {
        EmailSendRuleDO rule = new EmailSendRuleDO();
        rule.setId(request.getRuleId());
        rule.setOrgCode(userContext.getOrgCode());
        rule.setRuleName(request.getRuleName());
        rule.setTemplateCode(request.getTemplateCode());
        rule.setRuleType(RuleTypeEnum.fromCode(request.getRuleType()));
        rule.setCronExpression(request.getCronExpression());
        rule.setFixedRate(request.getFixedRate());
        rule.setFixedDelay(request.getFixedDelay());
        rule.setRecipients(request.getRecipients());
        rule.setCc(request.getCc());
        rule.setBcc(request.getBcc());
        rule.setStartTime(request.getStartTime());
        rule.setEndTime(request.getEndTime());
        rule.setMaxExecutions(request.getMaxExecutions());
        return rule;
    }

    /**
     * 生成规则代码
     * 格式：RULE + 随机8位数字
     */
    private String generateRuleCode() {
        return "RULE" + String.format("%08d", (int)(Math.random() * 100000000));
    }

    /**
     * 生成规则名称
     * 格式：Rule_[规则代码]_[时间戳]
     */
    private String generateRuleName(String ruleCode) {
        return String.format("Rule_%s_%s", 
            ruleCode,
            LocalDateTime.now().format(RULE_NAME_DATE_FORMATTER));
    }
} 