package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatusEnum;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * 邮件发送记录领域对象
 * 该对象代表了一个邮件发送记录的业务实体，包含了邮件的基本信息和业务行为。
 * 不包含任何持久化相关的字段，这些字段由基础设施层处理。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailSendRecordDO {
    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 模板ID
     */
    private String templateId;

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
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 变量
     */
    @Builder.Default
    private Map<String, Object> variables = new HashMap<>();

    /**
     * 状态：待发送、发送中、发送成功、发送失败
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
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
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
    public boolean canRetry() {
        return status == EmailStatusEnum.FAILED && (retryCount == null || retryCount < 3);
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

    /**
     * 获取发件人邮箱
     */
    public String getFromEmail() {
        return sender;
    }

    /**
     * 获取发件人名称
     */
    public String getFromName() {
        return sender;
    }

    /**
     * 获取收件人邮箱
     */
    public String getToEmail() {
        return recipient;
    }

    /**
     * 获取收件人名称
     */
    public String getToName() {
        return recipient;
    }

    /**
     * 获取抄送列表
     */
    public List<String> getCcList() {
        return cc != null ? List.of(cc.split(",")) : List.of();
    }

    /**
     * 获取密送列表
     */
    public List<String> getBccList() {
        return bcc != null ? List.of(bcc.split(",")) : List.of();
    }

    /**
     * 检查是否是HTML内容
     */
    public boolean isHtmlContent() {
        return content != null && content.trim().toLowerCase().startsWith("<!doctype html") || 
               content != null && content.trim().toLowerCase().startsWith("<html");
    }

    public boolean isPending() {
        return status == EmailStatusEnum.PENDING;
    }

    public boolean isSending() {
        return status == EmailStatusEnum.SENDING;
    }

    public boolean isFinished() {
        return status == EmailStatusEnum.SENT || status == EmailStatusEnum.FAILED;
    }

    public boolean isError() {
        return status == EmailStatusEnum.FAILED;
    }

    public static EmailSendRecordDO createPendingRecord() {
        EmailSendRecordDO record = new EmailSendRecordDO();
        record.setStatus(EmailStatusEnum.PENDING);
        return record;
    }

    public static EmailSendRecordDO createSendingRecord() {
        EmailSendRecordDO record = new EmailSendRecordDO();
        record.setStatus(EmailStatusEnum.SENDING);
        return record;
    }
} 