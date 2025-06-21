package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.loopnurture.users.infra.po.MarketingUserPO;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaMarketingUserMapper extends JpaRepository<MarketingUserPO, String> {
    Optional<MarketingUserPO> findBySystemUserId(Long systemUserId);
    Optional<MarketingUserPO> findByUserUniq(String userUniq);
    Optional<MarketingUserPO> findByPrimaryEmail(String primaryEmail);
    Optional<MarketingUserPO> findByPhone(String phone);
    List<MarketingUserPO> findByCurrentOrgCode(String orgCode);
    boolean existsByUserUniq(String userUniq);
    boolean existsByPrimaryEmail(String primaryEmail);
    boolean existsByPhone(String phone);
    Optional<MarketingUserPO> findByOauthUserIdAndAuthType(String oauthUserId, Integer authType);
} 