package org.springframework.samples.loopnurture.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.users.domain.enums.UserRoleEnum;
import org.springframework.samples.loopnurture.users.domain.model.UserOrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.UserOrganizationRepository;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.domain.repository.MarketingUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户-组织关联服务
 */
@Service
@RequiredArgsConstructor
public class UserOrganizationService {

    private final UserOrganizationRepository userOrgRepository;
    private final OrganizationRepository organizationRepository;
    private final MarketingUserRepository userRepository;

    /**
     * 添加用户到组织
     *
     * @param systemUserId 系统用户ID
     * @param orgId 组织ID
     * @param role 用户角色
     * @param operatorId 操作人ID
     * @return 用户-组织关联信息
     */
    @Transactional
    public UserOrganizationDO addUserToOrganization(Long systemUserId, String orgId, UserRoleEnum role, String operatorId) {
        // 验证用户和组织是否存在
        if (!userRepository.findBySystemUserId(systemUserId).isPresent()) {
            throw new IllegalArgumentException("User not found: " + systemUserId);
        }
        if (!organizationRepository.findById(orgId).isPresent()) {
            throw new IllegalArgumentException("Organization not found: " + orgId);
        }

        // 检查是否已存在关联
        if (userOrgRepository.existsBySystemUserIdAndOrgId(systemUserId, orgId)) {
            throw new IllegalStateException("User is already a member of the organization");
        }

        // 创建新的关联
        UserOrganizationDO userOrg = new UserOrganizationDO();
        userOrg.setSystemUserId(systemUserId);
        userOrg.setOrgId(orgId);
        userOrg.setRole(role);
        userOrg.setCreatedAt(LocalDateTime.now());
        userOrg.setUpdatedAt(LocalDateTime.now());
        userOrg.setCreatedBy(operatorId);
        userOrg.setUpdatedBy(operatorId);

        return userOrgRepository.save(userOrg);
    }

    /**
     * 更新用户角色
     *
     * @param systemUserId 系统用户ID
     * @param orgId 组织ID
     * @param newRole 新角色
     * @param operatorId 操作人ID
     * @return 更新后的用户-组织关联信息
     */
    @Transactional
    public UserOrganizationDO updateUserRole(Long systemUserId, String orgId, UserRoleEnum newRole, String operatorId) {
        UserOrganizationDO userOrg = userOrgRepository.findBySystemUserIdAndOrgId(systemUserId, orgId)
            .orElseThrow(() -> new IllegalArgumentException("User-Organization relationship not found"));

        userOrg.setRole(newRole);
        userOrg.setUpdatedAt(LocalDateTime.now());
        userOrg.setUpdatedBy(operatorId);

        return userOrgRepository.save(userOrg);
    }

    /**
     * 从组织中移除用户
     *
     * @param systemUserId 系统用户ID
     * @param orgId 组织ID
     */
    @Transactional
    public void removeUserFromOrganization(Long systemUserId, String orgId) {
        if (!userOrgRepository.findBySystemUserIdAndOrgId(systemUserId, orgId).isPresent()) {
            throw new IllegalArgumentException("User-Organization relationship not found");
        }
        userOrgRepository.deleteBySystemUserIdAndOrgId(systemUserId, orgId);
    }

    /**
     * 获取用户的所有组织关联
     *
     * @param systemUserId 系统用户ID
     * @return 用户的组织关联列表
     */
    public List<UserOrganizationDO> getUserOrganizations(Long systemUserId) {
        return userOrgRepository.findBySystemUserId(systemUserId);
    }

    /**
     * 获取组织的所有用户关联
     *
     * @param orgId 组织ID
     * @return 组织的用户关联列表
     */
    public List<UserOrganizationDO> getOrganizationUsers(String orgId) {
        return userOrgRepository.findByOrgId(orgId);
    }

    /**
     * 获取用户在组织中的角色
     *
     * @param systemUserId 系统用户ID
     * @param orgId 组织ID
     * @return 用户角色（如果存在）
     */
    public Optional<UserRoleEnum> getUserRole(Long systemUserId, String orgId) {
        return userOrgRepository.findBySystemUserIdAndOrgId(systemUserId, orgId)
            .map(UserOrganizationDO::getRole);
    }
} 