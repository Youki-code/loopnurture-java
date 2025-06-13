package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRecordRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 邮件服务
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailSendRuleRepository ruleRepository;
    private final MarketingEmailTemplateRepository templateRepository;
    private final EmailSendRecordRepository sendRecordRepository;
    private final UserQueryService userQueryService;

    /**
     * 执行邮件规则
     */
    public void executeRule(String ruleId) {
        // 查询规则
        EmailSendRuleDO rule = ruleRepository.findById(ruleId);
        if (rule == null) {
            return;
        }

        // 查询模板
        MarketingEmailTemplateDO template = templateRepository.getByTemplateId(rule.getTemplateId());
        if (template == null) {
            return;
        }

        // 获取目标用户
        List<Map<String, Object>> targetUsers = getTargetUsers(rule);
        
        // 创建发送记录
        for (Map<String, Object> user : targetUsers) {
            EmailSendRecordDO sendRecord = createSendRecord(rule, template, user);
            sendRecordRepository.save(sendRecord);
        }

        // 更新规则执行信息
        updateRuleExecutionInfo(rule);
    }

    /**
     * 获取目标用户
     */
    private List<Map<String, Object>> getTargetUsers(EmailSendRuleDO rule) {
        return userQueryService.executeQuery(rule.getUserQuery());
    }

    /**
     * 创建发送记录
     */
    private EmailSendRecordDO createSendRecord(EmailSendRuleDO rule, MarketingEmailTemplateDO template, Map<String, Object> user) {
        EmailSendRecordDO record = new EmailSendRecordDO();
        record.setOrgCode(rule.getOrgCode());
        record.setTemplateId(template.getTemplateId());
        record.setRecipient(user.get("email").toString());
        record.setVariables(user);
        record.setStatus(org.springframework.samples.loopnurture.mail.domain.enums.EmailStatusEnum.PENDING);
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        return record;
    }

    /**
     * 更新规则执行信息
     */
    private void updateRuleExecutionInfo(EmailSendRuleDO rule) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExecutionTime = calculateNextExecutionTime(rule, now);
        ruleRepository.updateExecutionInfo(
            rule.getId(),
            now,
            nextExecutionTime,
            rule.getExecutionCount() + 1
        );
    }

    /**
     * 计算下次执行时间
     */
    private LocalDateTime calculateNextExecutionTime(EmailSendRuleDO rule, LocalDateTime now) {
        // 根据调度类型和值计算下次执行时间
        // TODO: 实现具体的调度逻辑
        return now.plusHours(1);
    }

    public void sendEmail(EmailSendRuleDO rule) {
        // 获取模板
        MarketingEmailTemplateDO template = templateRepository.getByTemplateId(rule.getTemplateId());
        if (template == null) {
            throw ResourceNotFoundException.templateNotFound(rule.getTemplateId());
        }

        // 创建发送记录
        EmailSendRecordDO record = new EmailSendRecordDO();
        record.setOrgCode(rule.getOrgCode());
        record.setTemplateId(template.getTemplateId());
        // ... existing code ...
    }
} 