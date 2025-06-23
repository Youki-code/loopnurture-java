package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.infra.po.AiStrategyPO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * AI 策略 JPA Mapper
 */
@Repository
public interface JpaAiStrategyMapper extends JpaRepository<AiStrategyPO, String> {

    /**
     * 根据策略类型和启用状态获取最新策略
     *
     * @param aiStrategyType 策略类型
     * @param enableStatus 启用状态
     */
    @Query("SELECT s FROM AiStrategyPO s WHERE s.aiStrategyType = :aiStrategyType AND s.enableStatus = :enableStatus ORDER BY s.createdAt DESC")
    Optional<AiStrategyPO> findTopByTypeAndStatus(@Param("aiStrategyType") Short aiStrategyType,
                                                 @Param("enableStatus") Short enableStatus);
} 