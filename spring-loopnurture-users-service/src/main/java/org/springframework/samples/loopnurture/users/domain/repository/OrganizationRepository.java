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
     * 根据组织代码查询组织，如果不存在返回 null
     */
    OrganizationDO findByOrgCode(String orgCode);

    /**
     * 检查组织代码是否存在
     */
    boolean existsByOrgCode(String orgCode);

    /**
     * 查询所有组织
     */
    List<OrganizationDO> findAll();

    /**
     * 保存组织信息
     */
    OrganizationDO save(OrganizationDO organization);

    /**
     * 软删除组织（按组织编码）
     */
    void deleteByOrgCode(String orgCode);
} 