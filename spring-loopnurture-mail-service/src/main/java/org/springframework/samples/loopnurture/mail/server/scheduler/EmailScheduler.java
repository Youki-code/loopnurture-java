package org.springframework.samples.loopnurture.mail.server.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.model.EmailRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailRuleRepository;
import org.springframework.samples.loopnurture.mail.service.EmailService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件调度器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final EmailRuleRepository ruleRepository;
    private final EmailService emailService;

    /**
     * 每分钟检查并执行邮件规则
     */
    @Scheduled(fixedRate = 60000)
    public void executeRules() {
        LocalDateTime now = LocalDateTime.now();
        List<EmailRuleDO> activeRules = ruleRepository.findRulesForExecution(now);

        for (EmailRuleDO rule : activeRules) {
            try {
                if (shouldExecuteRule(rule)) {
                    emailService.executeRule(rule.getId());
                }
            } catch (Exception e) {
                log.error("Failed to execute email rule: {}", rule.getId(), e);
            }
        }
    }

    /**
     * 检查规则是否应该执行
     */
    private boolean shouldExecuteRule(EmailRuleDO rule) {
        if (!rule.getIsActive()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExecutionTime = rule.getNextExecutionTime();
        return nextExecutionTime == null || !nextExecutionTime.isAfter(now);
    }
} 