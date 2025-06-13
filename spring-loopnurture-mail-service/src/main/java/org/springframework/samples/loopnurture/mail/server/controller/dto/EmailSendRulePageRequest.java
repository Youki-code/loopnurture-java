package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 邮件发送规则分页查询请求DTO
 */
@Data
public class EmailSendRulePageRequest {
    
    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 规则类型
     */
    private Integer ruleType;

    /**
     * 收件人类型
     */
    private Integer recipientType;

    /**
     * 启用状态列表：1-启用，0-禁用
     */
    private List<Integer> enableStatusList;

    /**
     * 页码
     */
    @NotNull(message = "Page number cannot be null")
    @Min(value = 1, message = "Page number must be greater than 0")
    private Integer pageNum;

    /**
     * 每页大小
     */
    @NotNull(message = "Page size cannot be null")
    @Min(value = 1, message = "Page size must be greater than 0")
    private Integer pageSize;
} 