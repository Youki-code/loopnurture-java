package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;

/**
 * 组织JPA数据访问接口
 */
@Repository
public interface JpaOrganizationMapper extends JpaRepository<OrganizationPO, Long> {
    
    /**
     * 根据用户 ID 查询其所属组织列表。
     *
     * 通过 `UserOrganizationPO` 关联表进行子查询，避免在 `OrganizationPO` 上出现不存在的属性映射。
     */
    @Query("""
        SELECT o FROM OrganizationPO o
        WHERE o.orgCode IN (
            SELECT uo.orgCode FROM UserOrganizationPO uo WHERE uo.systemUserId = :systemUserId
        )
    """)
    List<OrganizationPO> findBySystemUserId(@Param("systemUserId") Long systemUserId);

    /**
     * 根据组织代码查询组织
     */
    Optional<OrganizationPO> findByOrgCode(String orgCode);

    /**
     * 检查组织代码是否存在
     */
    boolean existsByOrgCode(String orgCode);

    @Modifying
    @Transactional
    @Query("update OrganizationPO o set o.deleted = true where o.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update OrganizationPO o set o.deleted = true where o.orgCode = :orgCode")
    void softDeleteByOrgCode(@Param("orgCode") String orgCode);
} 