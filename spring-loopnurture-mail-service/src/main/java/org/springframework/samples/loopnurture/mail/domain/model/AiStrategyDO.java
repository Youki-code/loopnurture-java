package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.samples.loopnurture.mail.domain.enums.AiStrategyTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;

import java.util.Date;

/**
 * AI 策略领域对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiStrategyDO {

    /**
     * 策略版本
     */
    private String aiStrategyVersion;

    /**
     * 策略类型
     */
    private AiStrategyTypeEnum aiStrategyType;

    /**
     * 策略内容（JSON 字符串）
     */
    private String aiStrategyContent;

    /**
     * 启用状态
     */
    private EnableStatusEnum enableStatus;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 逻辑删除
     */
    private Boolean deleted;
} 