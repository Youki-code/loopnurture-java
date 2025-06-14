package org.springframework.samples.loopnurture.mail.server.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.service.EmailExecuteService;

import java.util.Date;
import java.util.List;

/**
 * 邮件调度器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final EmailExecuteService executeService;

    /**
     * 每分钟检查并执行邮件规则
     */
    @Scheduled(fixedRate = 60000)
    public void executeRules() {
        executeService.executeDueRules();
    }
} 