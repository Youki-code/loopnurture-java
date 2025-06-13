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
 * 邮件发送记录JPA Mapper接口
 */
public interface JpaEmailSendRecordMapper extends JpaRepository<EmailSendRecordPO, String> {

    /**
     * 根据组织编码和状态分页查询
     */
    Page<EmailSendRecordPO> findByOrgCodeAndStatus(String orgCode, Integer status, Pageable pageable);

    /**
     * 根据组织编码和发送时间范围分页查询
     */
    Page<EmailSendRecordPO> findByOrgCodeAndSentAtBetween(String orgCode, LocalDateTime start, LocalDateTime end, Pageable pageable);

    /**
     * 根据组织编码和模板ID分页查询
     */
    Page<EmailSendRecordPO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable);

    /**
     * 查询需要重试的记录
     */
    List<EmailSendRecordPO> findByStatusAndRetryCountLessThan(Integer status, int maxRetries);

    /**
     * 统计组织的发送记录数量
     */
    long countByOrgCodeAndStatus(String orgCode, Integer status);

    /**
     * 根据组织编码分页查询
     */
    Page<EmailSendRecordPO> findByOrgCode(String orgCode, Pageable pageable);

    /**
     * 根据模板ID分页查询
     */
    Page<EmailSendRecordPO> findByTemplateId(String templateId, Pageable pageable);

    /**
     * 查询需要重试的记录
     */
    @Query("SELECT e FROM EmailSendRecordPO e WHERE e.status = :status AND e.retryCount < :maxRetries " +
           "AND e.createdAt >= :now")
    List<EmailSendRecordPO> findRecordsForRetry(@Param("status") Integer status,
                                               @Param("maxRetries") int maxRetries, 
                                               @Param("now") LocalDateTime now);

    /**
     * 统计组织在指定时间范围内的发送记录数量
     */
    long countByOrgCodeAndStatusAndSentAtBetween(String orgCode, Integer status, 
                                                LocalDateTime start, LocalDateTime end);

    Page<EmailSendRecordPO> findByRuleId(String ruleId, Pageable pageable);

    @Query("SELECT e FROM EmailSendRecordPO e WHERE e.retryCount <= :maxRetryCount " +
           "AND e.status = 0 AND e.nextRetryTime <= :currentTime")
    List<EmailSendRecordPO> findRecordsForRetry(@Param("maxRetryCount") int maxRetryCount,
                                               @Param("currentTime") LocalDateTime currentTime);

    long countByOrgCodeAndStatusAndSendTimeBetween(String orgCode, Integer status,
                                                  LocalDateTime startTime, LocalDateTime endTime);

    // 新增：根据组织ID统计发送记录
    long countByOrgIdAndStatusAndSentAtBetween(String orgId, Integer status,
                                               LocalDateTime start, LocalDateTime end);

    // 新增：按状态、重试次数和创建时间查询
    List<EmailSendRecordPO> findByStatusAndRetryCountLessThanAndCreatedAtBefore(Integer status,
                                                                               int maxRetries,
                                                                               LocalDateTime beforeTime);

    /**
     * 逻辑删除发送记录
     */
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE EmailSendRecordPO e SET e.deleted = true WHERE e.id = :id")
    void softDeleteById(@Param("id") String id);
} 