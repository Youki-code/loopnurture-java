package org.springframework.samples.loopnurture.mail.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 邮件发送记录扩展信息 VO
 * 存放发送人、收件人、变量等灵活字段，避免污染 EmailSendRecordDO 的主干字段。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailSendRecordExtendsInfoVO implements Serializable {

    /** 发件人 */
    private String sender;

    /** 收件人列表 */
    private java.util.List<String> recipient;

    /** 抄送列表 */
    private java.util.List<String> cc;

    /** 密送列表 */
    private java.util.List<String> bcc;

    /** 失败原因 */
    private String errorMessage;

    /** 重试次数 */
    private Integer retryCount;

    /** 邮件主题 */
    private String subject;

    /** 邮件内容 */
    private String content;
} 