package org.springframework.samples.loopnurture.users.domain.repository;

import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import java.util.List;
import java.util.Optional;

/**
 * 组织仓储接口
 */
public interface OrganizationRepository {
    
    /**
     * 根据用户ID查询关联的组织列表
     *
     * @param systemUserId 系统用户ID
     * @return 组织列表
     */
    List<OrganizationDO> findByUserId(Long systemUserId);

    /**
     * 根据组织ID查询组织详情
     *
     * @param orgId 组织ID
     * @return 组织详情
     */
    Optional<OrganizationDO> findById(String orgId);

    /**
     * 查询所有组织
     */
    List<OrganizationDO> findAll();

    /**
     * 保存组织信息
     */
    OrganizationDO save(OrganizationDO organization);

    /**
     * 删除组织
     */
    void deleteById(String orgId);
} 