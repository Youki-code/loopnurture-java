package org.springframework.samples.loopnurture.mail.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailSendLogDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailSendLogRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.service.MarketingEmailService;
import org.springframework.samples.loopnurture.mail.service.EmailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarketingEmailServiceImpl implements MarketingEmailService {

    private final MarketingEmailRuleRepository ruleRepository;
    private final MarketingEmailTemplateRepository templateRepository;
    private final MarketingEmailSendLogRepository sendLogRepository;
    private final EmailSender emailSender;
    private final TemplateEngine templateEngine;

    @Override
    public List<MarketingEmailSendLogDO> executeEmailRule(String ruleId) {
        // 获取规则信息
        Optional<MarketingEmailRuleDO> ruleOpt = ruleRepository.findById(ruleId);
        if (ruleOpt.isEmpty()) {
            return new ArrayList<>();
        }

        MarketingEmailRuleDO rule = ruleOpt.get();
        // 检查规则是否启用
        if (rule.getStatus() != 1) {
            return new ArrayList<>();
        }

        // 获取邮件模板
        Optional<MarketingEmailTemplateDO> templateOpt = templateRepository.findById(rule.getTemplateId());
        if (templateOpt.isEmpty()) {
            return new ArrayList<>();
        }
        MarketingEmailTemplateDO template = templateOpt.get();

        // TODO: 根据规则的targetAudience获取目标用户列表
        List<Map<String, Object>> targetUsers = getTargetUsers(rule);
        
        List<MarketingEmailSendLogDO> sendLogs = new ArrayList<>();
        for (Map<String, Object> user : targetUsers) {
            MarketingEmailSendLogDO sendLog = createSendLog(rule, template, user);
            sendLogs.add(emailSender.sendEmail(sendLog));
        }

        return sendLogs;
    }

    @Override
    public MarketingEmailSendLogDO sendMarketingEmail(MarketingEmailSendLogDO emailLog) {
        return emailSender.sendEmail(emailLog);
    }

    private List<Map<String, Object>> getTargetUsers(MarketingEmailRuleDO rule) {
        // TODO: 实现根据规则的targetAudience获取目标用户的逻辑
        return new ArrayList<>();
    }

    private MarketingEmailSendLogDO createSendLog(MarketingEmailRuleDO rule, MarketingEmailTemplateDO template, Map<String, Object> user) {
        MarketingEmailSendLogDO sendLog = new MarketingEmailSendLogDO();
        sendLog.setRuleId(rule.getRuleId());
        sendLog.setTemplateId(template.getTemplateId());
        sendLog.setOrgId(rule.getOrgId());
        sendLog.setFromEmail(template.getFromEmail());
        sendLog.setFromName(template.getFromName());
        sendLog.setToEmail((String) user.get("email"));
        sendLog.setToName((String) user.get("name"));
        sendLog.setSubject(template.getSubject());
        sendLog.setContent(renderTemplate(template.getContent(), user));
        sendLog.setStatus(0);
        sendLog.setRetryCount(0);
        sendLog.setCreatedAt(LocalDateTime.now());
        sendLog.setUpdatedAt(LocalDateTime.now());
        return sendLogRepository.save(sendLog);
    }

    private String renderTemplate(String templateContent, Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        return templateEngine.process(templateContent, context);
    }
} 