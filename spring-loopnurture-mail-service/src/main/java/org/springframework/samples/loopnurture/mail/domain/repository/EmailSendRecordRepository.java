package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件发送记录仓储接口
 */
public interface EmailSendRecordRepository {
    
    /**
     * 保存发送记录
     */
    EmailSendRecordDO save(EmailSendRecordDO record);

    /**
     * 根据ID查询发送记录
     */
    Optional<EmailSendRecordDO> findById(String id);

    /**
     * 查询组织的发送记录
     */
    Page<EmailSendRecordDO> findByOrgId(String orgId, Pageable pageable);

    /**
     * 根据模板代码分页查询发送记录
     *
     * @param templateCode 模板代码
     * @param pageable 分页参数
     * @return 发送记录分页列表
     */
    Page<EmailSendRecordDO> findByTemplateCode(String templateCode, Pageable pageable);

    /**
     * 查询需要重试的发送记录
     */
    List<EmailSendRecordDO> findLogsForRetry(int maxRetries, LocalDateTime now);

    /**
     * 统计组织在指定时间范围内的发送记录数量
     */
    long countByOrgIdAndStatusAndSendTimeBetween(String orgId, Integer status, LocalDateTime start, LocalDateTime end);
} 