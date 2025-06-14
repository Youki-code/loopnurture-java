package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatusEnum;
import java.util.Date;
import java.util.List;
import org.springframework.samples.loopnurture.mail.domain.model.vo.EmailSendRecordExtendsInfoVO;

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
     * 状态：待发送、发送中、发送成功、发送失败
     */
    private EmailStatusEnum status;

    /**
     * 扩展信息
     */
    private EmailSendRecordExtendsInfoVO extendsInfo;

    /**
     * 发送时间
     */
    private Date sentAt;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 创建人
     */
    private String createdBy;

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
        this.sentAt = new Date();
    }

    /**
     * 标记为发送失败状态
     */
    public void markAsFailed(String error) {
        this.status = EmailStatusEnum.FAILED;
        if (this.extendsInfo == null) {
            this.extendsInfo = new EmailSendRecordExtendsInfoVO();
        }
        this.extendsInfo.setErrorMessage(error);
        Integer current = this.extendsInfo.getRetryCount();
        this.extendsInfo.setRetryCount(current == null ? 1 : current + 1);
    }

    /**
     * 检查是否可以重试
     */
    public boolean canRetry() {
        Integer rc = extendsInfo != null ? extendsInfo.getRetryCount() : null;
        return status == EmailStatusEnum.FAILED && (rc == null || rc < 3);
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
        return extendsInfo != null ? extendsInfo.getSender() : null;
    }

    /**
     * 获取发件人名称
     */
    public String getFromName() {
        return getFromEmail();
    }

    /**
     * 获取收件人邮箱
     */
    public String getToEmail() {
        if(extendsInfo==null || extendsInfo.getRecipient()==null || extendsInfo.getRecipient().isEmpty()) return null;
        return extendsInfo.getRecipient().get(0);
    }

    /**
     * 获取收件人名称
     */
    public String getToName() {
        return getToEmail();
    }

    /**
     * 获取抄送列表
     */
    public List<String> getCcList() {
        return extendsInfo != null && extendsInfo.getCc()!=null ? extendsInfo.getCc() : List.of();
    }

    /**
     * 获取密送列表
     */
    public List<String> getBccList() {
        return extendsInfo != null && extendsInfo.getBcc()!=null ? extendsInfo.getBcc() : List.of();
    }

    /**
     * 获取主题
     */
    public String getSubject() {
        return extendsInfo != null ? extendsInfo.getSubject() : null;
    }

    /**
     * 获取内容
     */
    public String getContent() {
        return extendsInfo != null ? extendsInfo.getContent() : null;
    }

    /**
     * 检查是否是HTML内容
     */
    public boolean isHtmlContent() {
        String c = getContent();
        return c != null && (c.trim().toLowerCase().startsWith("<!doctype html") || c.trim().toLowerCase().startsWith("<html"));
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