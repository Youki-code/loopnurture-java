package org.springframework.samples.loopnurture.mail.domain.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendRulePageQueryDTO {
    /**
     * 页码
     */
    @Builder.Default
    private int pageNum = 0;

    /**
     * 每页大小
     */
    @Builder.Default
    private int pageSize = 20;

    /**
     * 启用状态列表
     */
    private List<Integer> enableStatusList;

    /* -------  新增筛选字段 ------- */
    /** 规则名称（模糊匹配） */
    private String ruleName;

    /** 模板 ID */
    private String templateId;

    /** 规则类型 */
    private Integer ruleType;

    /** 收件人类型 */
    private Integer recipientType;

    /** 组织编码 */
    private String orgCode;
} 