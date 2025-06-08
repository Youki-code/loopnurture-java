package org.springframework.samples.loopnurture.mail.domain.model.vo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 营销邮件模板扩展信息值对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingEmailTemplateExtendsVO {


    /**
     * 模板版本号
     */
    private String version;

    /**
     * 模板作者
     */
    private String author;

    /**
     * 模板预览图URL
     */
    private String previewImageUrl;

    /**
     * 模板示例数据
     */
    private String sampleData;

    /**
     * 最后测试时间
     */
    private String lastTestedAt;

    /**
     * 模板评分（1-5）
     */
    private Integer rating;

    /**
     * 使用说明
     */
    private String instructions;

    /**
     * 其他自定义属性
     */
    private Object customProperties;
} 