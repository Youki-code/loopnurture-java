package org.springframework.samples.loopnurture.mail.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRecordRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.mail.exception.MailSendException;
import org.springframework.samples.loopnurture.mail.exception.ValidationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateQueryService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 邮件发送服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendService {
    
    private final SendGrid sendGrid;
    private final EmailSendRecordRepository sendRecordRepository;
    private final EmailSendRuleRepository sendRuleRepository;
    private final EmailTemplateQueryService templateQueryService;

    /**
     * 发送邮件
     */
    @Transactional
    public EmailSendRecordDO sendEmail(EmailSendRecordDO emailRecord) {
        try {
            // 标记为发送中
            emailRecord.markAsSending();

            // 构建SendGrid邮件对象
            Email from = new Email(emailRecord.getFromEmail(), emailRecord.getFromName());
            Email to = new Email(emailRecord.getToEmail(), emailRecord.getToName());
            Content content = new Content(
                emailRecord.isHtmlContent() ? "text/html" : "text/plain", 
                emailRecord.getContent()
            );
            Mail mail = new Mail(from, emailRecord.getSubject(), to, content);

            // 添加抄送
            List<String> ccList = emailRecord.getCcList();
            if (ccList != null && !ccList.isEmpty()) {
                for (String cc : ccList) {
                    mail.personalization.get(0).addCc(new Email(cc));
                }
            }

            // 添加密送
            List<String> bccList = emailRecord.getBccList();
            if (bccList != null && !bccList.isEmpty()) {
                for (String bcc : bccList) {
                    mail.personalization.get(0).addBcc(new Email(bcc));
                }
            }

            // 发送邮件
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            // 检查发送结果
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                emailRecord.markAsSent();
            } else {
                emailRecord.markAsFailed("SendGrid API error: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (IOException e) {
            log.error("Failed to send email", e);
            emailRecord.markAsFailed("SendGrid API error: " + e.getMessage());
            throw new MailSendException("Failed to send email", e);
        }

        return createSendRecord(emailRecord);
    }

    /**
     * 使用模板发送邮件
     */
    @Transactional
    public EmailSendRecordDO sendTemplateEmail(String templateId, String recipient, String recipientName, Map<String, Object> variables) {
        // 获取模板
        MarketingEmailTemplateDO template = templateQueryService.getTemplate(templateId);
        
        // 检查模板状态
        if (!template.isAvailable()) {
            throw new ValidationException("Template is not available for sending");
        }
        
        // 构建邮件日志
        EmailSendRecordDO emailRecord = EmailSendRecordDO.builder()
            .templateId(template.getTemplateCode())
            .recipient(recipient)
            .subject(template.getSubjectTemplate())
            .content(template.getContentTemplate())
            .variables(variables)
            .build();

        return sendEmail(emailRecord);
    }

    // 发送日志相关方法
    @Transactional
    public EmailSendRecordDO createSendRecord(EmailSendRecordDO record) {
        return sendRecordRepository.save(record);
    }

    @Transactional
    public EmailSendRecordDO updateSendRecord(EmailSendRecordDO record) {
        return sendRecordRepository.save(record);
    }

    public EmailSendRecordDO getSendRecord(String recordId) {
        EmailSendRecordDO record = sendRecordRepository.findById(recordId);
        if(record==null){
            throw new ResourceNotFoundException("Send record not found");
        }
        return record;
    }

    public Page<EmailSendRecordDO> getOrgSendRecords(String orgId, Pageable pageable) {
        return sendRecordRepository.findByOrgId(orgId, pageable);
    }

    public Page<EmailSendRecordDO> getRuleSendRecords(String ruleId, Pageable pageable) {
        return sendRecordRepository.findByRuleId(ruleId, pageable);
    }

    public Page<EmailSendRecordDO> getTemplateSendRecords(String templateId, Pageable pageable) {
        return sendRecordRepository.findByTemplateId(templateId, pageable);
    }

    public List<EmailSendRecordDO> findRecordsForRetry(int maxRetries, LocalDateTime now) {
        return sendRecordRepository.findRecordsForRetry(maxRetries, now);
    }

    public long countOrgSendRecords(String orgId, Integer status, LocalDateTime start, LocalDateTime end) {
        return sendRecordRepository.countByOrgIdAndStatusAndSendTimeBetween(orgId, status, start, end);
    }

    @Transactional
    public void retryFailedRecords() {
        List<EmailSendRecordDO> failedRecords = findRecordsForRetry(3, LocalDateTime.now());
        for (EmailSendRecordDO record : failedRecords) {
            try {
                sendEmail(record);
            } catch (Exception e) {
                log.error("Failed to retry sending email", e);
            }
        }
    }

    // 发送规则相关方法
    @Transactional
    public EmailSendRuleDO createRule(EmailSendRuleDO rule) {
        return sendRuleRepository.save(rule);
    }

    @Transactional
    public EmailSendRuleDO updateRule(EmailSendRuleDO rule) {
        if (!sendRuleRepository.existsById(rule.getId())) {
            throw new ResourceNotFoundException("Send rule not found");
        }
        return sendRuleRepository.save(rule);
    }

    @Transactional
    public void deleteRule(String ruleId) {
        if (!sendRuleRepository.existsById(ruleId)) {
            throw new ResourceNotFoundException("Send rule not found");
        }
        sendRuleRepository.deleteById(ruleId);
    }

    public EmailSendRuleDO getRule(String ruleId) {
        EmailSendRuleDO rule = sendRuleRepository.findById(ruleId);
        if(rule==null){
            throw new ResourceNotFoundException("Send rule not found");
        }
        return rule;
    }

    public Page<EmailSendRuleDO> getOrgRules(String orgId, Pageable pageable) {
        return sendRuleRepository.findByOrgId(orgId, pageable);
    }

    public boolean existsByRuleName(String orgId, String ruleName) {
        return sendRuleRepository.existsByOrgIdAndRuleName(orgId, ruleName);
    }

    @Transactional
    public List<EmailSendRecordDO> executeRule(String ruleId) {
        EmailSendRuleDO rule = getRule(ruleId);
        
        // 获取模板并检查状态
        MarketingEmailTemplateDO template = templateQueryService.getTemplate(rule.getTemplateId());
        if (!template.isAvailable()) {
            log.warn("Rule {} skipped: template {} is not available", rule.getId(), template.getId());
            return List.of();
        }
        
        // TODO: Implement rule execution logic
        return List.of();
    }

    @Transactional
    public void executeAllRules() {
        List<EmailSendRuleDO> rules = sendRuleRepository.findAll();
        for (EmailSendRuleDO rule : rules) {
            try {
                executeRule(rule.getId());
            } catch (Exception e) {
                log.error("Failed to execute rule: {}", rule.getId(), e);
            }
        }
    }
} 