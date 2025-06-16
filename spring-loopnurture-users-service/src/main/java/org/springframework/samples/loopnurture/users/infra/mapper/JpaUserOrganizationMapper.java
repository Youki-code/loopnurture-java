package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.loopnurture.users.infra.po.UserOrganizationPO;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserOrganizationMapper extends JpaRepository<UserOrganizationPO, Long> {
    List<UserOrganizationPO> findBySystemUserId(Long systemUserId);
    List<UserOrganizationPO> findByOrgCode(String orgCode);
    Optional<UserOrganizationPO> findBySystemUserIdAndOrgCode(Long systemUserId, String orgCode);
    void deleteBySystemUserIdAndOrgCode(Long systemUserId, String orgCode);
    void deleteBySystemUserId(Long systemUserId);
    void deleteByOrgCode(String orgCode);
    boolean existsBySystemUserIdAndOrgCode(Long systemUserId, String orgCode);
} 