package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 营销邮件模板分页查询请求DTO
 */
@Data
public class MarketingEmailTemplatePageRequest {

    /**
     * 组织编码
     */
    @Size(max = 50, message = "Organization code length cannot exceed 50")
    private String orgCode;

    /**
     * 模板代码
     */
    @Size(max = 50, message = "Template code length cannot exceed 50")
    private String templateCode;

    /**
     * 模板名称
     */
    @Size(max = 100, message = "Template name length cannot exceed 100")
    private String templateName;

    /**
     * 内容类型：1-文本，2-HTML
     */
    private Integer contentType;

    /**
     * 启用状态列表：1-启用，0-禁用
     */
    private List<Integer> enableStatusList;

    /**
     * 页码，从0开始
     */
    private Integer pageNumber = 0;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField = "createdAt";

    /**
     * 排序方向：asc-升序，desc-降序
     */
    private String sortDirection = "desc";

    /**
     * 转换为Pageable对象
     */
    public Pageable toPageable() {
        Sort sort = Sort.by(
            Sort.Direction.fromString(sortDirection),
            sortField
        );
        return PageRequest.of(pageNumber, pageSize, sort);
    }
} 