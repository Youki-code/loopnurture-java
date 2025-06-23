package org.springframework.samples.loopnurture.mail.infra.po;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import java.util.Date;

/**
 * AI 策略持久化对象
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "ai_strategy")
@Where(clause = "deleted = false")
public class AiStrategyPO {

    /**
     * 策略版本（主键）
     */
    @Id
    @Column(name = "ai_strategy_version", length = 50)
    private String aiStrategyVersion;

    /**
     * 策略类型
     */
    @Column(name = "ai_strategy_type", columnDefinition = "SMALLINT")
    private Short aiStrategyType;

    /**
     * 策略内容
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ai_strategy_content", columnDefinition = "jsonb")
    private String aiStrategyContent;

    /**
     * 启用状态
     */
    @Column(name = "enable_status", columnDefinition = "SMALLINT")
    private Short enableStatus;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 逻辑删除
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
} 