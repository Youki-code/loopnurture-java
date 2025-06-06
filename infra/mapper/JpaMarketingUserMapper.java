package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.loopnurture.users.infra.po.MarketingUserPO;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaMarketingUserMapper extends JpaRepository<MarketingUserPO, String> {
    Optional<MarketingUserPO> findByUserUniq(String userUniq);
    Optional<MarketingUserPO> findByPrimaryEmail(String primaryEmail);
    Optional<MarketingUserPO> findByPhone(String phone);
    List<MarketingUserPO> findByOrgId(String orgId);
    boolean existsByUserUniq(String userUniq);
    boolean existsByPrimaryEmail(String primaryEmail);
    boolean existsByPhone(String phone);
} 