package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.mail.domain.model.vo.MarketingEmailTemplateExtendsInfoVO;
import java.util.Date;

/**
 * 营销邮件模板响应DTO
 */
@Data
public class MarketingEmailTemplateResponse {
    
    /**
     * 主键ID
     */
    private String id;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 模板ID
     */
    private String templateId;

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
     * 输入内容
     */
    private String inputContent;

    /**
     * AI策略版本
     */
    private String aiStrategyVersion;

    /**
     * 启用状态：1-启用，0-禁用
     */
    private Integer enableStatus;

    /**
     * 扩展信息
     */
    private MarketingEmailTemplateExtendsInfoVO extendsInfo;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 更新人ID
     */
    private String updatedBy;

    /** 公司名称 */
    private String companyName;

    /** 邮件目的 */
    private String emailPurpose;
} 