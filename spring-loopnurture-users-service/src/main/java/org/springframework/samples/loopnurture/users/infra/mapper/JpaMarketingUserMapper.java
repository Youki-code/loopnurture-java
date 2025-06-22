package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.users.infra.po.MarketingUserPO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaMarketingUserMapper extends JpaRepository<MarketingUserPO, Long> {
    Optional<MarketingUserPO> findBySystemUserId(Long systemUserId);
    Optional<MarketingUserPO> findByUserUniq(String userUniq);
    Optional<MarketingUserPO> findByPrimaryEmail(String primaryEmail);
    Optional<MarketingUserPO> findByPhone(String phone);
    List<MarketingUserPO> findByCurrentOrgCode(String orgCode);
    boolean existsByUserUniq(String userUniq);
    boolean existsByPrimaryEmail(String primaryEmail);
    boolean existsByPhone(String phone);
    Optional<MarketingUserPO> findByOauthUserIdAndAuthType(String oauthUserId, Short authType);

    @Modifying
    @Transactional
    @Query("update MarketingUserPO m set m.deleted = true where m.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update MarketingUserPO m set m.deleted = true where m.systemUserId = :systemUserId")
    void softDeleteBySystemUserId(@Param("systemUserId") Long systemUserId);
} 