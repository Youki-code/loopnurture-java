package org.springframework.samples.loopnurture.users.domain.model;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.EmailTemplateStatusEnum;
import java.time.LocalDateTime;

/**
 * 邮件模板领域对象
 */
@Data
public class EmailTemplateDO {
    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板编码，用于业务标识
     */
    private String templateCode;

    /**
     * 模板主题
     */
    private String subject;

    /**
     * 模板内容，支持HTML和变量替换
     */
    private String content;

    /**
     * 模板参数定义，JSON格式
     * 例如：{"userName": "用户名", "verifyCode": "验证码"}
     */
    private String parameterDefinition;

    /**
     * 模板状态
     */
    private EmailTemplateStatusEnum status;

    /**
     * 所属组织ID
     */
    private String orgId;

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