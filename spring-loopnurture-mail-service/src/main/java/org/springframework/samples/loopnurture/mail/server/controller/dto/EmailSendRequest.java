package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 * 邮件发送请求DTO
 */
@Data
public class EmailSendRequest {
    
    /**
     * 模板ID
     */
    @NotNull(message = "Template ID cannot be null")
    @Size(max = 50, message = "Template ID length cannot exceed 50")
    private String templateId;

    /**
     * 发件人邮箱
     */
    @NotBlank(message = "发件人邮箱不能为空")
    private String fromEmail;

    /**
     * 收件人邮箱列表
     */
    @NotNull(message = "收件人邮箱列表不能为空")
    private String[] toEmails;

    /**
     * 抄送邮箱列表
     */
    private String[] ccEmails;

    /**
     * 密送邮箱列表
     */
    private String[] bccEmails;

    /**
     * 模板参数
     */
    private Map<String, Object> templateParams;
} 