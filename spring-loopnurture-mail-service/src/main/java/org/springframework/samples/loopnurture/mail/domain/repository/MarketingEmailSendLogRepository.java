package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailSendLogDO;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销邮件发送日志仓储接口
 */
public interface MarketingEmailSendLogRepository extends JpaRepository<MarketingEmailSendLogDO, String> {
    /**
     * 根据规则ID查询发送日志列表
     */
    List<MarketingEmailSendLogDO> findByRuleId(String ruleId);

    /**
     * 根据模板ID查询发送日志列表
     */
    List<MarketingEmailSendLogDO> findByTemplateId(String templateId);

    /**
     * 根据组织ID和状态查询发送日志列表
     */
    List<MarketingEmailSendLogDO> findByOrgIdAndStatus(String orgId, Integer status);

    /**
     * 查询需要重试的发送日志
     */
    @Query("SELECT l FROM MarketingEmailSendLogDO l WHERE l.status = 3 AND l.nextRetryTime <= :beforeTime AND l.retryCount < :maxRetryCount")
    List<MarketingEmailSendLogDO> findRetryableLogs(@Param("beforeTime") LocalDateTime beforeTime, @Param("maxRetryCount") Integer maxRetryCount);

    /**
     * 批量更新发送状态
     */
    @Modifying
    @Query("UPDATE MarketingEmailSendLogDO l SET l.status = :status WHERE l.logId IN :logIds")
    void updateStatus(@Param("logIds") List<String> logIds, @Param("status") Integer status);

    /**
     * 更新重试信息
     */
    @Modifying
    @Query("UPDATE MarketingEmailSendLogDO l SET l.retryCount = :retryCount, l.nextRetryTime = :nextRetryTime, l.failReason = :failReason WHERE l.logId = :logId")
    void updateRetryInfo(@Param("logId") String logId, @Param("retryCount") Integer retryCount, @Param("nextRetryTime") LocalDateTime nextRetryTime, @Param("failReason") String failReason);
} 