package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.RuleTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.model.vo.EmailSendRuleExtendsInfoVO;

import java.util.Date;
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
     * 扩展信息
     */
    private EmailSendRuleExtendsInfoVO extendsInfo;


    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

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
    private Date lastExecutionTime;

    /**
     * 下次执行时间
     */
    private Date nextExecutionTime;

    /**
     * 是否激活
     */
    private EnableStatusEnum enableStatus;


    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 更新人ID
     */
    private String updatedBy;


    /**************************操作方法**************************/

    public void modifyRule(String ruleName, String templateId, Integer ruleType,  Date startTime, Date endTime, Integer maxExecutions, EnableStatusEnum enableStatus,String description,List<String> recipients,List<String> cc,List<String> bcc,String cronExpression,Long fixedRate,Long fixedDelay) {
        this.ruleName = ruleName;
        this.templateId = templateId;
        this.ruleType = ruleType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxExecutions = maxExecutions;
        this.enableStatus = enableStatus;
        if(this.extendsInfo==null){
            this.extendsInfo = new EmailSendRuleExtendsInfoVO();
        }
        this.extendsInfo.setDescription(description);
        this.extendsInfo.setRecipients(recipients);
        this.extendsInfo.setCc(cc);
        this.extendsInfo.setBcc(bcc);
        this.extendsInfo.setCronExpression(cronExpression);
        this.extendsInfo.setFixedRate(fixedRate);
        this.extendsInfo.setFixedDelay(fixedDelay);
        this.updatedAt = new Date();
        this.updatedBy = UserContext.getUserId();

    }



    /**************************查询方法**************************/

    /**
     * 检查规则是否可执行
     *
     * @return true 如果规则可以执行
     */
    public boolean isExecutable() {
        if (enableStatus != EnableStatusEnum.ENABLED) {
            return false;
        }

        Date now = new Date();
        if (startTime != null && now.before(startTime)) {
            return false;
        }
        if (endTime != null && now.after(endTime)) {
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
    public boolean isEnable() {
        return enableStatus != null && enableStatus.equals(EnableStatusEnum.ENABLED);
    }

    /**
     * 记录执行完成
     *
     * @param nextTime 下次执行时间
     */
    public void recordExecution(Date nextTime) {
        this.lastExecutionTime = new Date();
        this.nextExecutionTime = nextTime;
        if (executionCount == null) {
            executionCount = 1;
        } else {
            executionCount++;
        }
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

} 