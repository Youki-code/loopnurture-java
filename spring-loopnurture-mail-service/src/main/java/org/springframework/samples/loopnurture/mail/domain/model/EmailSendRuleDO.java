package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import org.springframework.samples.loopnurture.mail.domain.enums.RuleTypeEnum;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送规则领域对象
 * 
 * 该对象代表了一个邮件定时发送规则的业务实体，包含了规则的基本信息和业务行为。
 * 与数据库实体的主要区别：
 * 1. 直接使用枚举类型而不是代码
 * 2. 提供了收件人列表的解析和验证方法
 * 3. 包含了规则执行管理的业务方法
 */
@Data
public class EmailSendRuleDO {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 规则代码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 模板代码
     */
    private String templateCode;

    /**
     * 规则类型：1-立即发送，2-定时发送，3-周期发送
     */
    private RuleTypeEnum ruleType;

    /**
     * Cron表达式（定时发送时必填）
     */
    private String cronExpression;

    /**
     * 固定频率，单位：毫秒（周期发送时必填）
     */
    private Long fixedRate;

    /**
     * 固定延迟，单位：毫秒（周期发送时必填）
     */
    private Long fixedDelay;

    /**
     * 收件人列表
     */
    private List<String> recipients;

    /**
     * 抄送人列表
     */
    private List<String> cc;

    /**
     * 密送人列表
     */
    private List<String> bcc;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 最大执行次数
     */
    private Integer maxExecutions;

    /**
     * 已执行次数
     */
    private Integer executionCount;

    /**
     * 上次执行时间
     */
    private LocalDateTime lastExecutionTime;

    /**
     * 下次执行时间
     */
    private LocalDateTime nextExecutionTime;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 规则描述
     */
    private String description;

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

    /**
     * 检查规则是否可执行
     *
     * @return true 如果规则可以执行
     */
    public boolean isEnabled() {
        return isActive != null && isActive &&
               (startTime == null || startTime.isBefore(LocalDateTime.now())) &&
               (endTime == null || endTime.isAfter(LocalDateTime.now())) &&
               (maxExecutions == null || executionCount < maxExecutions);
    }

    /**
     * 检查规则是否启用
     *
     * @return true 如果规则启用
     */
    public boolean isActive() {
        return isActive != null && isActive;
    }

    /**
     * 记录执行完成
     *
     * @param nextTime 下次执行时间
     */
    public void recordExecution(LocalDateTime nextTime) {
        this.lastExecutionTime = LocalDateTime.now();
        this.nextExecutionTime = nextTime;
        this.executionCount = (this.executionCount == null ? 0 : this.executionCount) + 1;
    }

    /**
     * 检查是否使用Cron表达式
     *
     * @return true 如果使用Cron表达式
     */
    public boolean isCronRule() {
        return ruleType == RuleTypeEnum.CRON;
    }

    /**
     * 检查是否使用固定频率
     *
     * @return true 如果使用固定频率
     */
    public boolean isFixedRateRule() {
        return ruleType == RuleTypeEnum.FIXED_RATE;
    }

    /**
     * 检查是否使用固定延迟
     *
     * @return true 如果使用固定延迟
     */
    public boolean isFixedDelayRule() {
        return ruleType == RuleTypeEnum.FIXED_DELAY;
    }
} 