package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.mail.exception.ValidationException;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.RuleTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.model.vo.EmailSendRuleExtendsInfoVO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateEmailSendRuleRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.UpdateEmailSendRuleRequest;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * 仅包含写操作的发送规则 Operate Service
 */
@Service
@RequiredArgsConstructor
public class EmailSendRuleOperateService {

    private final EmailSendRuleRepository ruleRepository;

    private final MarketingEmailTemplateRepository marketingEmailTemplateRepository;



    @Transactional
    public EmailSendRuleDO createRule(CreateEmailSendRuleRequest request) {
        // 判断ruleType在不在枚举之中
        if(RuleTypeEnum.fromCode(request.getRuleType()) == null){
            throw new ValidationException("Rule type not found");
        }
        // 判断收件人不能为空
        if(request.getRecipients() == null || request.getRecipients().isEmpty()){
            throw new ValidationException("Recipients cannot be empty");
        }
        // 判断templateId是否存在
        MarketingEmailTemplateDO template = marketingEmailTemplateRepository.getByTemplateId(request.getTemplateId());
        if (template == null) {
            throw new ValidationException("Template not found");
        }
        String orgCode = UserContext.getOrgCode();
        checkDuplicateName(orgCode, request.getRuleName(), null);

        // ruleId 生成规则：时间yyMMHHmmss + UUID
        String ruleId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMHHmmss")) + UUID.randomUUID().toString();
        // name没有的话设置成ruleId
        EmailSendRuleExtendsInfoVO extendsInfo = EmailSendRuleExtendsInfoVO.builder()
                .recipients(request.getRecipients())
                .cc(request.getCc())
                .bcc(request.getBcc())
                .description(request.getDescription())
                .cronExpression(request.getCronExpression())
                .fixedRate(request.getFixedRate())
                .fixedDelay(request.getFixedDelay())
                .subject(request.getSubject())
                .build();

        // 如果是即时发送，则maxExecutions设置为1
        if(request.getRuleType() == RuleTypeEnum.IMMEDIATE.getCode()){
            request.setMaxExecutions(1);
            // 开始时间和结束时间设置成2个小时
            request.setStartTime(new Date());
            request.setEndTime(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000));
        }
        // 如果是定时发送，则maxExecutions设置为null
        if(request.getRuleType() == RuleTypeEnum.CRON.getCode()){
            // 判断cronExpression不能为空
            if(request.getCronExpression() == null || request.getCronExpression().isEmpty()){
                throw new ValidationException("Cron expression cannot be empty");
            }
            // 判断cronExpression是否符合cron表达式格式
            if(!CronExpression.isValidExpression(request.getCronExpression())){
                throw new ValidationException("Cron expression is not valid");
            }
        }
        EmailSendRuleDO rule = EmailSendRuleDO.builder()
                .orgCode(orgCode)
                .ruleId(ruleId)
                .ruleName(request.getRuleName()!=null?request.getRuleName():ruleId)
                .templateId(request.getTemplateId())
                .ruleType(request.getRuleType())
                .extendsInfo(extendsInfo)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxExecutions(request.getMaxExecutions())
                .executionCount(0)
                .createdAt(new Date())
                .updatedAt(new Date())
                .createdBy(UserContext.getUserId())
                .updatedBy(UserContext.getUserId())
                .enableStatus(EnableStatusEnum.ENABLED)
                .build();
        // 如果是即时发送，则maxExecutions设置为1
        if(request.getRuleType() == RuleTypeEnum.IMMEDIATE.getCode()){
            rule.setMaxExecutions(1);
            // 开始时间和结束时间设置成2个小时
            rule.setStartTime(new Date());
            rule.setEndTime(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000));
            // 设置nextExecutionTime为当前时间
            rule.setNextExecutionTime(new Date());
        }
        return ruleRepository.save(rule);
    }

    @Transactional
    public EmailSendRuleDO modifyRule(UpdateEmailSendRuleRequest request) {
        EmailSendRuleDO rule = ruleRepository.findByRuleId(request.getRuleId());
        if(rule==null){
            throw new ResourceNotFoundException("Rule not found");
        }

        checkDuplicateName(rule.getOrgCode(), request.getRuleName(), rule.getRuleId());

        // 更新字段
        if(request.getRuleName()!=null) rule.setRuleName(request.getRuleName());
        if(request.getTemplateId()!=null) rule.setTemplateId(request.getTemplateId());
        if(request.getRuleType()!=null) rule.setRuleType(request.getRuleType());
        rule.setStartTime(request.getStartTime());
        rule.setEndTime(request.getEndTime());
        rule.setMaxExecutions(request.getMaxExecutions());
        rule.setEnableStatus(EnableStatusEnum.fromCode(request.getEnableStatus()));
        // 扩展信息
        EmailSendRuleExtendsInfoVO ext = rule.getExtendsInfo();
        if(ext==null){ ext = new EmailSendRuleExtendsInfoVO(); }
        ext.setRecipients(request.getRecipients());
        ext.setCc(request.getCc());
        ext.setBcc(request.getBcc());
        ext.setSubject(request.getSubject());
        ext.setCronExpression(request.getCronExpression());
        ext.setFixedRate(request.getFixedRate());
        ext.setFixedDelay(request.getFixedDelay());
        rule.setExtendsInfo(ext);
        rule.setUpdatedAt(new Date());
        
        return ruleRepository.save(rule);
    }

    @Transactional
    public void deleteRule(String ruleId) {
        ruleRepository.deleteByRuleId(ruleId);
    }

    @Transactional
    public void executeRule(String ruleId) {
        EmailSendRuleDO rule = ruleRepository.findByRuleId(ruleId);
        if (rule == null) {
            throw new ResourceNotFoundException("Rule not found");
        }

        // 如果规则未启用或已超出执行限制则直接返回
        if (!rule.canExecute()) {
            return; // 静默跳过
        }

        // 此处应调用真正的邮件发送逻辑；为了测试，简单记录执行信息
        Date now = new Date();
        rule.updateExecutionInfo(now, null);

        ruleRepository.save(rule);
    }

    private void checkDuplicateName(String orgCode, String ruleName, String excludeId) {
        if (ruleName == null) return;
        EmailSendRuleDO emailSendRuleDO = ruleRepository.findByOrgCodeAndRuleName(orgCode, ruleName);
        if (emailSendRuleDO != null && !emailSendRuleDO.getRuleId().equals(excludeId)) {
            throw new ValidationException("Rule name already exists");
        }
    }
} 