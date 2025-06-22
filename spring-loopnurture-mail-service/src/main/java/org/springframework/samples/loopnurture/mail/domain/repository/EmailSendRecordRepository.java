package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.repository.dto.EmailSendRecordPageQueryDTO;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

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
    void update(Long id, EmailSendRecordDO record);

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
     * 根据组织ID查找记录
     */
    Page<EmailSendRecordDO> findByOrgCode(String orgCode, Pageable pageable);

    /**
     * 根据组织编码、状态和时间范围查询邮件发送记录
     */
    Page<EmailSendRecordDO> findByOrgCodeAndStatusAndSentAtBetween(String orgCode, EmailStatusEnum status, 
                                                                 LocalDateTime startTime, LocalDateTime endTime, 
                                                                 Pageable pageable);

    /**
     * 根据模板ID查询邮件发送记录
     */
    Page<EmailSendRecordDO> findByTemplateId(String templateId, Pageable pageable);

    /**
     * 根据业务 recordId 获取记录
     */
    EmailSendRecordDO findByRecordId(String recordId);

    /**
     * 根据ID删除记录
     */
    void deleteById(Long id);

    @Deprecated // 请勿在应用层使用
    EmailSendRecordDO findById(Long id);
} 