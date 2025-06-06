package org.springframework.samples.loopnurture.users.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import java.util.List;

/**
 * 组织JPA仓储接口
 */
public interface JpaOrganizationRepository extends JpaRepository<OrganizationPO, String> {

    /**
     * 根据用户ID查询关联的组织列表
     */
    @Query("SELECT o FROM OrganizationPO o JOIN UserOrganizationPO uo ON o.orgId = uo.orgId WHERE uo.systemUserId = :systemUserId")
    List<OrganizationPO> findBySystemUserId(@Param("systemUserId") Long systemUserId);
} 