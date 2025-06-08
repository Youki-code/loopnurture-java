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
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendLogDO;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.repository.EmailSendLogRepository;
import org.springframework.samples.loopnurture.mail.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.mail.exception.MailSendException;
import org.springframework.samples.loopnurture.mail.exception.ValidationException;
import org.springframework.transaction.annotation.Transactional;

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
    private final EmailSendLogRepository sendLogRepository;
    private final EmailSendRuleRepository sendRuleRepository;
    private final EmailTemplateService templateService;

    /**
     * 发送邮件
     */
    @Transactional
    public EmailSendLogDO sendEmail(EmailSendLogDO emailLog) {
        try {
            // 标记为发送中
            emailLog.markAsSending();

            // 构建SendGrid邮件对象
            Email from = new Email(emailLog.getFromEmail(), emailLog.getFromName());
            Email to = new Email(emailLog.getToEmail(), emailLog.getToName());
            Content content = new Content(
                emailLog.isHtmlContent() ? "text/html" : "text/plain", 
                emailLog.getContent()
            );
            Mail mail = new Mail(from, emailLog.getSubject(), to, content);

            // 添加抄送
            List<String> ccList = emailLog.getCcList();
            if (ccList != null && !ccList.isEmpty()) {
                for (String cc : ccList) {
                    mail.personalization.get(0).addCc(new Email(cc));
                }
            }

            // 添加密送
            List<String> bccList = emailLog.getBccList();
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
                emailLog.markAsSent();
            } else {
                emailLog.markAsFailed("SendGrid API error: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (IOException e) {
            log.error("Failed to send email", e);
            emailLog.markAsFailed("SendGrid API error: " + e.getMessage());
            throw new MailSendException("Failed to send email", e);
        }

        return createSendLog(emailLog);
    }

    /**
     * 使用模板发送邮件
     */
    @Transactional
    public EmailSendLogDO sendTemplateEmail(String templateCode, String recipient, String recipientName, Map<String, Object> variables) {
        // 获取模板
        MarketingEmailTemplateDO template = templateService.getTemplateByCode(templateCode);
        
        // 检查模板状态
        if (!template.isAvailable()) {
            throw new ValidationException("Template is not available for sending");
        }
        
        // 构建邮件日志
        EmailSendLogDO emailLog = EmailSendLogDO.builder()
            .templateCode(template.getTemplateCode())
            .toEmail(recipient)
            .toName(recipientName)
            .subject(template.getSubjectTemplate())
            .content(template.getContentTemplate())
            .isHtmlContent(template.isHtmlContent())
            .variables(variables)
            .build();

        return sendEmail(emailLog);
    }

    // 发送日志相关方法
    @Transactional
    public EmailSendLogDO createSendLog(EmailSendLogDO log) {
        return sendLogRepository.save(log);
    }

    @Transactional
    public EmailSendLogDO updateSendLog(EmailSendLogDO log) {
        if (!sendLogRepository.existsById(log.getId())) {
            throw new ResourceNotFoundException("Send log not found");
        }
        return sendLogRepository.save(log);
    }

    public EmailSendLogDO getSendLog(String logId) {
        return sendLogRepository.findById(logId)
            .orElseThrow(() -> new ResourceNotFoundException("Send log not found"));
    }

    public Page<EmailSendLogDO> getOrgSendLogs(String orgId, Pageable pageable) {
        return sendLogRepository.findByOrgId(orgId, pageable);
    }

    public Page<EmailSendLogDO> getRuleSendLogs(String ruleId, Pageable pageable) {
        return sendLogRepository.findByRuleId(ruleId, pageable);
    }

    @Override
    public Page<EmailSendLogDO> getTemplateSendLogs(String templateCode, Pageable pageable) {
        return sendLogRepository.findByTemplateCode(templateCode, pageable);
    }

    public List<EmailSendLogDO> findLogsForRetry(int maxRetries, LocalDateTime now) {
        return sendLogRepository.findLogsForRetry(maxRetries, now);
    }

    public long countOrgSendLogs(String orgId, Integer status, LocalDateTime start, LocalDateTime end) {
        return sendLogRepository.countByOrgIdAndStatusAndSendTimeBetween(orgId, status, start, end);
    }

    @Transactional
    public void retryFailedLogs() {
        List<EmailSendLogDO> failedLogs = findLogsForRetry(3, LocalDateTime.now());
        for (EmailSendLogDO log : failedLogs) {
            try {
                sendEmail(log);
            } catch (Exception e) {
                log.error("Failed to retry sending email: {}", log.getId(), e);
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
        return sendRuleRepository.findById(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException("Send rule not found"));
    }

    public Page<EmailSendRuleDO> getOrgRules(String orgId, Pageable pageable) {
        return sendRuleRepository.findByOrgId(orgId, pageable);
    }

    public boolean existsByRuleName(String orgId, String ruleName) {
        return sendRuleRepository.existsByOrgIdAndRuleName(orgId, ruleName);
    }

    @Transactional
    public List<EmailSendLogDO> executeRule(String ruleId) {
        EmailSendRuleDO rule = getRule(ruleId);
        
        // 获取模板并检查状态
        MarketingEmailTemplateDO template = templateService.getTemplate(rule.getTemplateId());
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