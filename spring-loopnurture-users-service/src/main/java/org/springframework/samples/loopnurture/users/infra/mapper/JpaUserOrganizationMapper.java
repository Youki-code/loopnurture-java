package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.loopnurture.users.infra.po.UserOrganizationPO;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserOrganizationMapper extends JpaRepository<UserOrganizationPO, Long> {
    List<UserOrganizationPO> findBySystemUserId(Long systemUserId);
    List<UserOrganizationPO> findByOrgId(String orgId);
    Optional<UserOrganizationPO> findBySystemUserIdAndOrgId(Long systemUserId, String orgId);
    void deleteBySystemUserIdAndOrgId(Long systemUserId, String orgId);
    void deleteBySystemUserId(Long systemUserId);
    void deleteByOrgId(String orgId);
    boolean existsBySystemUserIdAndOrgId(Long systemUserId, String orgId);
} 