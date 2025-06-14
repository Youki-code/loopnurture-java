package org.springframework.samples.loopnurture.mail.domain.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * 邮件发送规则扩展信息 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailSendRuleExtendsInfoVO implements Serializable {
    /**
     * 收件人列表
     */
    private List<String> recipients;

    /**
     * 抄送列表
     */
    private List<String> cc;

    /**
     * 密送列表
     */
    private List<String> bcc;
    /**
     * 规则描述
     */
    private String description;
    
    /** 
     * Cron表达式
     * 
    */
    private String cronExpression;

        /**
     * 固定频率，单位：毫秒（周期发送时必填）
     */
    private Long fixedRate;

    /**
     * 固定延迟，单位：毫秒（周期发送时必填）
     */
    private Long fixedDelay;
} 