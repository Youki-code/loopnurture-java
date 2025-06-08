package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 营销邮件模板响应DTO
 */
@Data
public class MarketingEmailTemplateResponse {
    
    /**
     * 模板ID
     */
    private String id;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 模板代码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 内容类型：1-文本，2-HTML
     */
    private Integer contentType;

    /**
     * 模板内容
     */
    private String contentTemplate;

    /**
     * AI策略版本
     */
    private String aiStrategyVersion;

    /**
     * 扩展信息
     */
    private Map<String, Object> extendsInfo;

    /**
     * 模板状态：1-草稿，2-已发布，3-已禁用，4-已删除
     */
    private Integer status;

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