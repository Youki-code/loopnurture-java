package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.loopnurture.mail.domain.enums.RuleTypeEnum;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailSendRuleDO {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 规则ID
     */
    private String ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 规则类型：1-定时，2-固定频率，3-固定延迟
     */
    private Integer ruleType;

    /**
     * Cron表达式（定时规则）
     */
    private String cronExpression;

    /**
     * 固定频率（毫秒）
     */
    private Long fixedRate;

    /**
     * 固定延迟（毫秒）
     */
    private Long fixedDelay;

    /**
     * 收件人列表
     */
    private String recipients;

    /**
     * 抄送列表
     */
    private String cc;

    /**
     * 密送列表
     */
    private String bcc;

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
     * 是否激活
     */
    private Boolean isActive;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 用户查询条件
     */
    @Builder.Default
    private Map<String, Object> userQuery = new HashMap<>();

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
    public boolean isExecutable() {
        if (!isActive) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        if (startTime != null && now.isBefore(startTime)) {
            return false;
        }
        if (endTime != null && now.isAfter(endTime)) {
            return false;
        }
        if (maxExecutions != null && executionCount != null && executionCount >= maxExecutions) {
            return false;
        }

        return true;
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
        return ruleType != null && ruleType.equals(RuleTypeEnum.CRON.getCode());
    }

    /**
     * 检查是否使用固定频率
     *
     * @return true 如果使用固定频率
     */
    public boolean isFixedRateRule() {
        return ruleType != null && ruleType.equals(RuleTypeEnum.FIXED_RATE.getCode());
    }

    /**
     * 检查是否使用固定延迟
     *
     * @return true 如果使用固定延迟
     */
    public boolean isFixedDelayRule() {
        return ruleType != null && ruleType.equals(RuleTypeEnum.FIXED_DELAY.getCode());
    }

    /**
     * 检查是否是立即执行规则
     *
     * @return true 如果是立即执行规则
     */
    public boolean isImmediateRule() {
        return ruleType != null && ruleType.equals(RuleTypeEnum.IMMEDIATE.getCode());
    }

    /**
     * 获取用户查询条件
     *
     * @return 用户查询条件
     */
    public Map<String, Object> getUserQuery() {
        return userQuery;
    }

    /**
     * 设置用户查询条件
     *
     * @param userQuery 用户查询条件
     */
    public void setUserQuery(Map<String, Object> userQuery) {
        this.userQuery = userQuery;
    }
} 