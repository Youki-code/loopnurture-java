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
     * Derived query: Spring Data will automatically add LIMIT 1 because of "First" keyword.
     */
    Optional<AiStrategyPO> findFirstByAiStrategyTypeAndEnableStatusOrderByCreatedAtDesc(Short aiStrategyType,
                                                                                       Short enableStatus);
} 