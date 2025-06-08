package org.springframework.samples.loopnurture.users.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import java.util.List;
import java.util.Optional;

/**
 * 组织仓储接口
 */
public interface OrganizationJpaMapper extends JpaRepository<OrganizationDO, String> {
    
    /**
     * 根据用户ID查询关联的组织列表
     *
     * @param systemUserId 系统用户ID
     * @return 组织列表
     */
    List<OrganizationDO> findByUserId(Long systemUserId);

    /**
     * 根据组织代码查询组织
     */
    Optional<OrganizationDO> findByOrgCode(String orgCode);

    /**
     * 检查组织代码是否存在
     */
    boolean existsByOrgCode(String orgCode);
} 