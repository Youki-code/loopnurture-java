package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRecordPO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送记录JPA Mapper
 */
public interface JpaEmailSendRecordMapper extends JpaRepository<EmailSendRecordPO, String> {
    
    /**
     * 查询组织的发送记录
     */
    Page<EmailSendRecordPO> findByOrgCodeOrderByCreatedAtDesc(String orgCode, Pageable pageable);

    /**
     * 根据模板代码查询发送记录
     */
    Page<EmailSendRecordPO> findByTemplateCodeOrderByCreatedAtDesc(String templateCode, Pageable pageable);

    /**
     * 查询需要重试的发送记录
     */
    @Query("SELECT r FROM EmailSendRecordPO r WHERE r.status = 4 " +
           "AND (r.retryCount IS NULL OR r.retryCount < :maxRetries) " +
           "AND r.updatedAt < :now")
    List<EmailSendRecordPO> findLogsForRetry(@Param("maxRetries") int maxRetries,
                                            @Param("now") LocalDateTime now);

    /**
     * 统计组织在指定时间范围内的发送记录数量
     */
    long countByOrgCodeAndStatusAndSentAtBetween(String orgCode, Integer status,
                                              LocalDateTime start, LocalDateTime end);
} 