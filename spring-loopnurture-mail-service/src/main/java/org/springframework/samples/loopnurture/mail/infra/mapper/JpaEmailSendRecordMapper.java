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
    Page<EmailSendRecordPO> findByOrgCodeAndSendTimeBetween(String orgCode, LocalDateTime start, LocalDateTime end, Pageable pageable);

    /**
     * 根据组织编码和模板ID分页查询
     */
    Page<EmailSendRecordPO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable);

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
     * 根据规则ID分页查询
     */
    Page<EmailSendRecordPO> findByRuleId(String ruleId, Pageable pageable);

    /**
     * 统计组织在指定时间范围内的发送记录数量
     */
    long countByOrgCodeAndStatusAndSendTimeBetween(String orgCode, Integer status,
                                                  LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据状态查询发送记录
     */
    List<EmailSendRecordPO> findByStatus(Integer status);

    /**
     * 逻辑删除发送记录
     */
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE EmailSendRecordPO e SET e.deleted = true WHERE e.id = :id")
    void softDeleteById(@Param("id") String id);
} 