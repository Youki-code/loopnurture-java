package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import lombok.Builder;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatusEnum;
import java.time.LocalDateTime;

/**
 * 邮件发送记录领域对象
 * 对应数据库表：email_send_record
 */
@Data
@Builder
public class EmailSendRecordDO {
    /**
     * 记录ID
     */
    private String id;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 模板代码
     */
    private String templateCode;

    /**
     * 发件人
     */
    private String sender;

    /**
     * 收件人
     */
    private String recipient;

    /**
     * 抄送
     */
    private String cc;

    /**
     * 密送
     */
    private String bcc;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 使用的变量值（JSONB格式）
     */
    private String variables;

    /**
     * 发送状态
     */
    private EmailStatusEnum status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 发送时间
     */
    private LocalDateTime sentAt;

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

    /**
     * 标记为待发送状态
     */
    public void markAsPending() {
        this.status = EmailStatusEnum.PENDING;
    }

    /**
     * 标记为发送中状态
     */
    public void markAsSending() {
        this.status = EmailStatusEnum.SENDING;
    }

    /**
     * 标记为发送成功状态
     */
    public void markAsSent() {
        this.status = EmailStatusEnum.SENT;
        this.sentAt = LocalDateTime.now();
    }

    /**
     * 标记为发送失败状态
     */
    public void markAsFailed(String error) {
        this.status = EmailStatusEnum.FAILED;
        this.errorMessage = error;
        this.retryCount = this.retryCount == null ? 1 : this.retryCount + 1;
    }

    /**
     * 检查是否可以重试
     */
    public boolean canRetry(int maxRetries) {
        return status == EmailStatusEnum.FAILED && 
               (retryCount == null || retryCount < maxRetries);
    }

    /**
     * 检查是否发送成功
     */
    public boolean isSent() {
        return status == EmailStatusEnum.SENT;
    }

    /**
     * 检查是否发送失败
     */
    public boolean isFailed() {
        return status == EmailStatusEnum.FAILED;
    }
} 