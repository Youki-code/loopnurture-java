package org.springframework.samples.loopnurture.mail.service;

import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailSendLogDO;

/**
 * 邮件发送器接口
 */
public interface EmailSender {
    /**
     * 发送邮件
     * @param emailLog 邮件日志对象
     * @return 更新后的邮件日志对象
     */
    MarketingEmailSendLogDO sendEmail(MarketingEmailSendLogDO emailLog);
} 