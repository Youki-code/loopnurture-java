package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRecordPO;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件发送记录JPA Mapper接口
 */
@Repository
public interface JpaEmailSendRecordMapper extends JpaRepository<EmailSendRecordPO, Long> {

    /**
     * 根据组织编码和状态查询邮件发送记录
     */
    Page<EmailSendRecordPO> findByOrgCodeAndStatus(String orgCode, Short status, Pageable pageable);

    /**
     * 根据组织编码和发送时间范围查询邮件发送记录
     */
    Page<EmailSendRecordPO> findByOrgCodeAndSendTimeBetween(String orgCode, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据组织编码和模板ID查询邮件发送记录
     */
    Page<EmailSendRecordPO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable);

    /**
     * 根据组织编码查询邮件发送记录
     */
    Page<EmailSendRecordPO> findByOrgCode(String orgCode, Pageable pageable);

    /**
     * 根据状态查询邮件发送记录
     */
    List<EmailSendRecordPO> findByStatus(Short status);

    /**
     * 根据组织编码和状态统计记录数量
     */
    @Query("SELECT COUNT(e) FROM EmailSendRecordPO e WHERE e.orgCode = :orgCode AND e.status = :status AND e.deleted = false")
    long countByOrgCodeAndStatus(@Param("orgCode") String orgCode, @Param("status") Short status);

    /**
     * 查找需要重试的邮件发送记录
     */
    @Query("SELECT e FROM EmailSendRecordPO e WHERE e.status = :status AND e.deleted = false")
    List<EmailSendRecordPO> findByStatus(@Param("status") Integer status);

    /**
     * 逻辑删除发送记录
     */
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE EmailSendRecordPO e SET e.deleted = true WHERE e.id = :id")
    void softDeleteById(@Param("id") Long id);
} 