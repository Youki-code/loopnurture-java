package org.springframework.samples.loopnurture.mail.domain.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * 营销邮件模板扩展信息值对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingEmailTemplateExtendsInfoVO {

    /**
     * 变量默认值
     */
    private Map<String, String> defaultVariables;

    /**
     * 变量描述
     */
    private Map<String, String> variableDescriptions;

    /**
     * 模板标签
     */
    private String[] tags;

    /**
     * 模板分类
     */
    private String category;

    /**
     * 模板备注
     */
    private String remark;

    /**
     * 邮件主题模板（可选）
     */
    private String subjectTemplate;

    /**
     * 用户输入内容
     */
    private String inputContent;

    /** 收件人列表 */
    private java.util.List<String> recipients;

    /** 抄送列表 */
    private java.util.List<String> cc;

    /** 密送列表 */
    private java.util.List<String> bcc;
} 