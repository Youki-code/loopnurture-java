package org.springframework.samples.loopnurture.mail.service;

import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailSendLogDO;
import java.util.List;

/**
 * 营销邮件服务接口
 */
public interface MarketingEmailService {
    
    /**
     * 执行邮件发送规则
     * @param ruleId 规则ID
     * @return 发送日志列表
     */
    List<MarketingEmailSendLogDO> executeEmailRule(String ruleId);

    /**
     * 发送单个营销邮件
     * @param emailLog 邮件发送记录
     * @return 更新后的发送记录
     */
    MarketingEmailSendLogDO sendMarketingEmail(MarketingEmailSendLogDO emailLog);
} 