package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.samples.loopnurture.mail.domain.model.EmailRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.EmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRecordRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 邮件服务
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRuleRepository ruleRepository;
    private final EmailTemplateRepository templateRepository;
    private final EmailSendRecordRepository sendRecordRepository;
    private final UserQueryService userQueryService;

    /**
     * 执行邮件规则
     */
    public void executeRule(String ruleId) {
        // 查询规则
        Optional<EmailRuleDO> ruleOpt = ruleRepository.findById(ruleId);
        if (!ruleOpt.isPresent()) {
            return;
        }

        EmailRuleDO rule = ruleOpt.get();
        
        // 查询模板
        Optional<EmailTemplateDO> templateOpt = templateRepository.findById(rule.getTemplateId());
        if (!templateOpt.isPresent()) {
            return;
        }

        EmailTemplateDO template = templateOpt.get();

        // 获取目标用户
        List<Map<String, Object>> targetUsers = getTargetUsers(rule);
        
        // 创建发送记录
        for (Map<String, Object> user : targetUsers) {
            EmailSendRecordDO sendRecord = createSendLog(rule, template, user);
            sendRecordRepository.save(sendRecord);
        }

        // 更新规则执行信息
        updateRuleExecutionInfo(rule);
    }

    /**
     * 获取目标用户
     */
    private List<Map<String, Object>> getTargetUsers(EmailRuleDO rule) {
        return userQueryService.executeQuery(rule.getUserQuery());
    }

    /**
     * 创建发送记录
     */
    private EmailSendRecordDO createSendLog(EmailRuleDO rule, EmailTemplateDO template, Map<String, Object> user) {
        EmailSendRecordDO record = new EmailSendRecordDO();
        record.setOrgCode(rule.getOrgCode());
        record.setTemplateId(template.getTemplateId());
        record.setRecipient(user.get("email").toString());
        record.setVariables(user);
        record.setStatus(0); // PENDING
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        return record;
    }

    /**
     * 更新规则执行信息
     */
    private void updateRuleExecutionInfo(EmailRuleDO rule) {
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
    private LocalDateTime calculateNextExecutionTime(EmailRuleDO rule, LocalDateTime now) {
        // 根据调度类型和值计算下次执行时间
        // TODO: 实现具体的调度逻辑
        return now.plusHours(1);
    }

    @Override
    public void sendEmail(EmailSendRuleDO rule) {
        // 获取模板
        Optional<EmailTemplateDO> templateOpt = templateRepository.findByTemplateId(rule.getTemplateId());
        if (templateOpt.isEmpty()) {
            throw ResourceNotFoundException.templateNotFound(rule.getTemplateId());
        }
        EmailTemplateDO template = templateOpt.get();

        // 创建发送记录
        EmailSendRecordDO record = new EmailSendRecordDO();
        record.setId(UUID.randomUUID().toString());
        record.setOrgCode(rule.getOrgCode());
        record.setTemplateId(template.getTemplateId());
        // ... existing code ...
    }
} 