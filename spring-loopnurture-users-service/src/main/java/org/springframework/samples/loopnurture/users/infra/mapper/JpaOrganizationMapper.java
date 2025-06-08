package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import java.util.List;
import java.util.Optional;

/**
 * 组织JPA数据访问接口
 */
public interface JpaOrganizationMapper extends JpaRepository<OrganizationPO, String> {
    
    /**
     * 根据用户ID查询关联的组织列表
     */
    List<OrganizationPO> findByUserId(Long systemUserId);

    /**
     * 根据组织代码查询组织
     */
    Optional<OrganizationPO> findByOrgCode(String orgCode);

    /**
     * 检查组织代码是否存在
     */
    boolean existsByOrgCode(String orgCode);
} 