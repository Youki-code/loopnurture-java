package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件发送记录仓储接口
 * 定义了邮件发送记录的持久化操作
 * 注意：领域对象不包含ID字段，ID的处理由基础设施层负责
 */
public interface EmailSendRecordRepository {
    
    /**
     * 保存邮件发送记录
     * 如果是新记录，会自动生成ID
     */
    EmailSendRecordDO save(EmailSendRecordDO record);

    /**
     * 更新邮件发送记录
     * @param id 要更新的记录ID
     * @param record 更新的内容
     */
    void update(String id, EmailSendRecordDO record);

    /**
     * 根据ID查找邮件发送记录
     * 注意：返回的领域对象中不包含ID信息
     */
    EmailSendRecordDO findById(String id);

    /**
     * 根据组织编码和状态查询邮件发送记录
     */
    Page<EmailSendRecordDO> findByOrgCodeAndStatus(String orgCode, EmailStatusEnum status, Pageable pageable);

    /**
     * 根据组织编码和时间范围查询邮件发送记录
     */
    Page<EmailSendRecordDO> findByOrgCodeAndSentAtBetween(String orgCode, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据组织编码和模板ID查询邮件发送记录
     */
    Page<EmailSendRecordDO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable);

    /**
     * 查找需要重试的邮件发送记录
     */
    List<EmailSendRecordDO> findRetryableRecords(int maxRetries);

    /**
     * 统计组织的邮件发送记录数量
     */
    long countByOrgCodeAndStatus(String orgCode, EmailStatusEnum status);

    /**
     * 删除邮件发送记录
     */
    void deleteById(String id);

    /**
     * 根据组织ID查找记录
     */
    Page<EmailSendRecordDO> findByOrgId(String orgId, Pageable pageable);

    /**
     * 根据规则ID查找记录
     */
    Page<EmailSendRecordDO> findByRuleId(String ruleId, Pageable pageable);

    /**
     * 根据模板ID查找记录
     */
    Page<EmailSendRecordDO> findByTemplateId(String templateId, Pageable pageable);

    /**
     * 查找需要重试的记录
     */
    List<EmailSendRecordDO> findRecordsForRetry(int maxRetryCount, LocalDateTime beforeTime);

    /**
     * 统计组织在指定时间段内的发送记录数量
     */
    long countByOrgIdAndStatusAndSendTimeBetween(String orgId, Integer status, LocalDateTime startTime, LocalDateTime endTime);

    long countByOrgIdAndStatusAndSentAtBetween(String orgId, Integer status, LocalDateTime startTime, LocalDateTime endTime);

    List<EmailSendRecordDO> findByStatusAndRetryCountLessThanAndCreatedAtBefore(Integer status, int maxRetries, LocalDateTime beforeTime);
} 