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

    /**
     * 检查规则是否可以执行
     */
    public boolean canExecute() {
        if (enableStatus != EnableStatusEnum.ENABLED) {
            return false;
        }

        Date now = new Date();
        
        // 检查开始时间
        if (startTime != null && now.before(startTime)) {
            return false;
        }
        
        // 检查结束时间
        if (endTime != null && now.after(endTime)) {
            return false;
        }
        
        // 检查最大执行次数
        if (maxExecutions != null && executionCount != null && executionCount >= maxExecutions) {
            return false;
        }
        
        return true;
    }

    /**
     * 更新执行信息
     */
    public void updateExecutionInfo(Date lastExecuteTime, Date nextExecuteTime) {
        this.lastExecutionTime = lastExecuteTime;
        this.nextExecutionTime = nextExecuteTime;
        if (this.executionCount == null) {
            this.executionCount = 0;
        }
        this.executionCount++;
        if(this.executionCount >= this.maxExecutions){
            this.enableStatus = EnableStatusEnum.DISABLED;
        }
        if(this.executionCount >= this.maxExecutions){
            this.enableStatus = EnableStatusEnum.DISABLED;
        }
    }

    /**
     * 启用规则
     */
    public void enable() {
        this.enableStatus = EnableStatusEnum.ENABLED;
    }

    /**
     * 禁用规则
     */
    public void disable() {
        this.enableStatus = EnableStatusEnum.DISABLED;
    }

    /**
     * 获取收件人列表
     */
    public List<String> getRecipients() {
        if (extendsInfo != null && extendsInfo.getRecipients() != null) {
            return extendsInfo.getRecipients();
        }
        return List.of();
    }

    /**
     * 获取抄送列表
     */
    public List<String> getCcList() {
        if (extendsInfo != null && extendsInfo.getCc() != null) {
            return extendsInfo.getCc();
        }
        return List.of();
    }

    /**
     * 获取密送列表
     */
    public List<String> getBccList() {
        if (extendsInfo != null && extendsInfo.getBcc() != null) {
            return extendsInfo.getBcc();
        }
        return List.of();
    }

    /**
     * 验证规则配置
     */
    public void validate() {
        if (orgCode == null || orgCode.trim().isEmpty()) {
            throw new IllegalArgumentException("组织编码不能为空");
        }
        
        if (ruleId == null || ruleId.trim().isEmpty()) {
            throw new IllegalArgumentException("规则ID不能为空");
        }
        
        if (ruleName == null || ruleName.trim().isEmpty()) {
            throw new IllegalArgumentException("规则名称不能为空");
        }
        
        if (templateId == null || templateId.trim().isEmpty()) {
            throw new IllegalArgumentException("模板ID不能为空");
        }
        
        if (ruleType == null) {
            throw new IllegalArgumentException("规则类型不能为空");
        }
        
        // 验证时间配置
        if (startTime != null && endTime != null && startTime.after(endTime)) {
            throw new IllegalArgumentException("开始时间不能晚于结束时间");
        }
        
        // 验证执行次数
        if (maxExecutions != null && maxExecutions <= 0) {
            throw new IllegalArgumentException("最大执行次数必须大于0");
        }
    }

    /**
     * 创建新规则时的初始化
     */
    public static EmailSendRuleDO create(String orgCode, String ruleId, String ruleName, 
                                       String templateId, Integer ruleType, 
                                       EmailSendRuleExtendsInfoVO extendsInfo) {
        EmailSendRuleDO rule = EmailSendRuleDO.builder()
                .orgCode(orgCode)
                .ruleId(ruleId)
                .ruleName(ruleName)
                .templateId(templateId)
                .ruleType(ruleType)
                .extendsInfo(extendsInfo)
                .enableStatus(EnableStatusEnum.ENABLED)
                .executionCount(0)
                .createdAt(new Date())
                .updatedAt(new Date())
                .createdBy(UserContext.getUserId())
                .updatedBy(UserContext.getUserId())
                .build();
        
        rule.validate();
        return rule;
    }
} 