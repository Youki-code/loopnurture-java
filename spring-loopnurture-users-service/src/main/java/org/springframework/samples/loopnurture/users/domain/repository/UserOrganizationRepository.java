package org.springframework.samples.loopnurture.users.domain.repository;

import org.springframework.samples.loopnurture.users.domain.model.UserOrganizationDO;
import java.util.List;
import java.util.Optional;

/**
 * 用户-组织关联仓储接口
 */
public interface UserOrganizationRepository {
    /**
     * 查找用户的所有组织关联
     *
     * @param systemUserId 系统用户ID
     * @return 用户的组织关联列表
     */
    List<UserOrganizationDO> findBySystemUserId(Long systemUserId);

    /**
     * 查找组织的所有用户关联
     *
     * @param orgId 组织ID
     * @return 组织的用户关联列表
     */
    List<UserOrganizationDO> findByOrgId(String orgId);

    /**
     * 查找特定的用户-组织关联
     *
     * @param systemUserId 系统用户ID
     * @param orgId 组织ID
     * @return 用户-组织关联（如果存在）
     */
    Optional<UserOrganizationDO> findBySystemUserIdAndOrgId(Long systemUserId, String orgId);

    /**
     * 保存用户-组织关联
     *
     * @param userOrganization 用户-组织关联信息
     * @return 保存后的用户-组织关联信息
     */
    UserOrganizationDO save(UserOrganizationDO userOrganization);

    /**
     * 删除用户-组织关联
     *
     * @param systemUserId 系统用户ID
     * @param orgId 组织ID
     */
    void deleteBySystemUserIdAndOrgId(Long systemUserId, String orgId);

    /**
     * 删除用户的所有组织关联
     *
     * @param systemUserId 系统用户ID
     */
    void deleteBySystemUserId(Long systemUserId);

    /**
     * 删除组织的所有用户关联
     *
     * @param orgId 组织ID
     */
    void deleteByOrgId(String orgId);

    /**
     * 检查用户-组织关联是否存在
     *
     * @param systemUserId 系统用户ID
     * @param orgId 组织ID
     * @return 是否存在
     */
    boolean existsBySystemUserIdAndOrgId(Long systemUserId, String orgId);
} 