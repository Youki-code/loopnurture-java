package org.springframework.samples.loopnurture.mail.server.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailRuleRepository;
import org.springframework.samples.loopnurture.mail.service.MarketingEmailService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销邮件定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MarketingEmailScheduler {

    private final MarketingEmailRuleRepository ruleRepository;
    private final MarketingEmailService marketingEmailService;

    /**
     * 每10分钟执行一次邮件发送任务
     */
    @Scheduled(fixedRate = 600000)
    public void executeMarketingEmailTasks() {
        log.info("Start executing marketing email tasks at {}", LocalDateTime.now());
        try {
            // 获取所有已启用的规则
            List<MarketingEmailRuleDO> activeRules = ruleRepository.findByOrgIdAndStatus(null, 1);
            
            for (MarketingEmailRuleDO rule : activeRules) {
                try {
                    // 检查是否满足发送时间条件
                    if (shouldExecuteRule(rule)) {
                        marketingEmailService.executeEmailRule(rule.getRuleId());
                    }
                } catch (Exception e) {
                    log.error("Failed to execute rule {}: {}", rule.getRuleId(), e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Failed to execute marketing email tasks: {}", e.getMessage(), e);
        }
        log.info("Finished executing marketing email tasks at {}", LocalDateTime.now());
    }

    private boolean shouldExecuteRule(MarketingEmailRuleDO rule) {
        try {
            // TODO: 根据规则的sendTimeConfig判断是否应该执行
            // 可以解析JSON配置，支持：
            // 1. 立即发送
            // 2. 定时发送（指定时间）
            // 3. 周期发送（每天、每周、每月的特定时间）
            return true;
        } catch (Exception e) {
            log.error("Failed to check rule execution time for {}: {}", rule.getRuleId(), e.getMessage());
            return false;
        }
    }
} 