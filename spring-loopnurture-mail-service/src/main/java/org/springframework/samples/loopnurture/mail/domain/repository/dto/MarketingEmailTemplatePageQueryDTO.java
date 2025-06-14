package org.springframework.samples.loopnurture.mail.domain.repository.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * 营销邮件模板分页查询 DTO
 */
@Data
@Builder
public class MarketingEmailTemplatePageQueryDTO {
    /**
     * 页码
     */
    @Builder.Default
    private int pageNum = 0;

    /**
     * 每页条数
     */
    @Builder.Default
    private int pageSize = 20;

    /**
     * 启用状态列表
     */
    private List<Integer> enableStatusList;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 模板名称（模糊匹配）
     */
    private String templateName;

    /**
     * 内容类型：1-文本，2-HTML
     */
    private Integer contentType;

} 