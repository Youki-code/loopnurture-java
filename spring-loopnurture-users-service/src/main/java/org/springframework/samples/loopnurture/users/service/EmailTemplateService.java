package org.springframework.samples.loopnurture.users.service;

import org.springframework.samples.loopnurture.users.domain.model.EmailTemplateDO;
import java.util.List;
import java.util.Map;

/**
 * 邮件模板服务接口
 */
public interface EmailTemplateService {
    
    /**
     * 创建邮件模板
     */
    EmailTemplateDO createTemplate(EmailTemplateDO template);

    /**
     * 更新邮件模板
     */
    EmailTemplateDO updateTemplate(EmailTemplateDO template);

    /**
     * 根据ID查询模板
     */
    EmailTemplateDO getTemplateById(String templateId);

    /**
     * 根据编码查询模板
     */
    EmailTemplateDO getTemplateByCode(String templateCode, String orgId);

    /**
     * 查询组织的所有模板
     */
    List<EmailTemplateDO> getTemplatesByOrgId(String orgId);

    /**
     * 删除模板
     */
    void deleteTemplate(String templateId);

    /**
     * 渲染模板内容
     */
    String renderTemplate(String templateId, Map<String, Object> parameters);
} 