package org.springframework.samples.loopnurture.users.service;

import org.springframework.samples.loopnurture.users.domain.model.EmailLogDO;
import org.springframework.samples.loopnurture.users.server.dto.EmailSendRequest;
import java.util.List;

/**
 * 邮件发送服务接口
 */
public interface EmailSendService {
    
    /**
     * 发送邮件
     */
    EmailLogDO sendEmail(EmailSendRequest request, String orgId);

    /**
     * 批量发送邮件
     */
    List<EmailLogDO> batchSendEmail(List<EmailSendRequest> requests, String orgId);

    /**
     * 重试发送失败的邮件
     */
    void retryFailedEmails();

    /**
     * 查询邮件发送记录
     */
    List<EmailLogDO> getEmailLogs(String orgId, int page, int size);

    /**
     * 取消待发送的邮件
     */
    void cancelEmail(String logId);

    /**
     * 获取发送记录详情
     */
    EmailLogDO getEmailLog(String logId);
} 