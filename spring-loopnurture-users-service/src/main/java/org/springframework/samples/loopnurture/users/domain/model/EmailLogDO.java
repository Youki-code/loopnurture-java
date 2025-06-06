package org.springframework.samples.loopnurture.users.domain.model;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.EmailSendStatusEnum;
import java.time.LocalDateTime;

/**
 * 邮件发送记录领域对象
 */
@Data
public class EmailLogDO {
    /**
     * 记录ID
     */
    private String logId;

    /**
     * 所属组织ID
     */
    private String orgId;

    /**
     * 使用的模板ID
     */
    private String templateId;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 收件人邮箱
     */
    private String toEmail;

    /**
     * 收件人姓名
     */
    private String toName;

    /**
     * 实际发送的内容
     */
    private String content;

    /**
     * 替换的参数，JSON格式
     */
    private String parameters;

    /**
     * 发送状态
     */
    private EmailSendStatusEnum status;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 更新人ID
     */
    private String updatedBy;
} 