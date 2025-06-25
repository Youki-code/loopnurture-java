package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.model.vo.EmailSendRecordExtendsInfoVO;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateQueryService;

import java.util.Date;
import java.util.List;

/**
 * 负责批量触发待执行邮件规则的服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailExecuteService {

    private final EmailSendRuleRepository ruleRepository;
    private final EmailSendService emailSendService;
    private final EmailTemplateQueryService templateQueryService;

    /**
     * 查询所有已启用且到达执行时间且未超出最大次数的规则并逐一执行。
     */
    public void executeDueRules() {
        Date now = new Date();
        List<EmailSendRuleDO> dueRules = ruleRepository.findRulesForExecution(now);
        for (EmailSendRuleDO rule : dueRules) {
            try {
                executeRule(rule.getRuleId());
            } catch (Exception e) {
                log.error("Failed to execute rule {}", rule.getRuleId(), e);
            }
        }
    }

    /**
     * 执行单条规则 - 使用@Transactional注解
     */
    @Transactional
    public void executeRule(String ruleId) {
        log.info("Starting transaction for rule: {}", ruleId);
        
        try {
            executeRuleInternal(ruleId);
            log.info("Transaction completed successfully for rule: {}", ruleId);
        } catch (Exception e) {
            log.error("Error executing rule {} - transaction will be rolled back", ruleId, e);
            throw e;
        }
    }

    /**
     * 内部执行规则逻辑
     */
    private void executeRuleInternal(String ruleId) {
        EmailSendRuleDO rule = ruleRepository.findByRuleId(ruleId);
        if (rule == null) {
            log.warn("Rule {} not found", ruleId);
            return;
        }

        // 获取模板并校验
        MarketingEmailTemplateDO template = templateQueryService.getTemplate(rule.getTemplateId());
        if (template == null || !template.isAvailable()) {
            log.warn("Rule {} skipped: template {} unavailable", rule.getRuleId(), rule.getTemplateId());
            return;
        }

        // 收件人 应该是从rule中获取
        List<String> recipients = rule.getRecipients();
        if (recipients.isEmpty()) {
            log.info("Rule {} has no recipients", rule.getRuleId());
            return;
        }

        for (String email : recipients) {
            // 收货人抄送人密送人
            List<String> cc = rule.getCcList();
            List<String> bcc = rule.getBccList();
            String subject = rule.getExtendsInfo()!=null && rule.getExtendsInfo().getSubject()!=null
                    ? rule.getExtendsInfo().getSubject()
                    : template.getSubjectTemplate();

            EmailSendRecordExtendsInfoVO info = EmailSendRecordExtendsInfoVO.builder()
                    .recipient(java.util.List.of(email))
                    .subject(subject)
                    .content(template.getContentTemplate())
                    .cc(cc)
                    .bcc(bcc)
                    .build();

            EmailSendRecordDO record = EmailSendRecordDO.builder()
                    .orgCode(rule.getOrgCode())
                    .templateId(template.getTemplateId())
                    .extendsInfo(info)
                    .build();

            try {
                emailSendService.sendEmail(record);
            } catch (Exception ex) {
                log.error("Failed sending email for rule {} recipient {}", rule.getRuleId(), email, ex);
            }
        }

        // 更新执行信息
        Date now = new Date();
        Date next = new Date(now.getTime() + 3600_000);
        rule.updateExecutionInfo(now, next);
        ruleRepository.save(rule);
    }

    private List<String> getRecipients(MarketingEmailTemplateDO template) {
        var ext = template.getExtendsInfo();
        return ext!=null && ext.getRecipients()!=null ? ext.getRecipients() : java.util.List.of();
    }
} 