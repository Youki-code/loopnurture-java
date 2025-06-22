package org.springframework.samples.loopnurture.users.service;

import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.domain.exception.OrganizationNotFoundException;
import org.springframework.samples.loopnurture.users.domain.exception.OrganizationUniqExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组织服务类
 * 提供组织相关的核心业务功能，包括组织的创建、查询、更新等操作
 *
 * @author loopnurture
 */
@Service
@Transactional
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * 查询所有组织
     * 获取系统中所有组织的列表
     *
     * @return 组织列表
     */
    @Transactional(readOnly = true)
    public List<OrganizationDO> findAll() {
        return organizationRepository.findAll();
    }

    /**
     * 根据ID查询组织
     * 获取指定组织的详细信息
     *
     * @param id 组织ID
     * @return 组织信息
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    @Transactional(readOnly = true)
    public OrganizationDO findById(String id) {
        OrganizationDO org = organizationRepository.findByOrgCode(id);
        if (org == null) {
            throw new OrganizationNotFoundException("Organization not found with code: " + id);
        }
        return org;
    }

    /**
     * 根据组织代码查询组织
     * 获取指定组织代码的组织信息
     *
     * @param orgCode 组织代码
     * @return 组织信息
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    @Transactional(readOnly = true)
    public OrganizationDO findByOrgCode(String orgCode) {
        OrganizationDO org = organizationRepository.findByOrgCode(orgCode);
        if (org == null) {
            throw new OrganizationNotFoundException("Organization not found with code: " + orgCode);
        }
        return org;
    }

    /**
     * 创建新组织
     * 创建一个新的组织，并进行必要的验证
     *
     * @param organization 组织信息
     * @return 创建后的组织信息
     * @throws OrganizationUniqExistsException 如果组织代码已存在
     */
    public OrganizationDO createOrganization(OrganizationDO organization) {
        if (organizationRepository.existsByOrgCode(organization.getOrgCode())) {
            throw new OrganizationUniqExistsException("Organization code already exists: " + organization.getOrgCode());
        }
        return organizationRepository.save(organization);
    }

    /**
     * 更新组织信息
     * 更新现有组织的信息
     *
     * @param orgCode 组织代码
     * @param organization 组织信息
     * @return 更新后的组织信息
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    public OrganizationDO updateOrganization(String orgCode, OrganizationDO organization) {
        OrganizationDO existing = findById(orgCode);
        existing.setOrgName(organization.getOrgName());
        existing.setPhone(organization.getPhone());
        existing.setEmail(organization.getEmail());
        existing.setWebsite(organization.getWebsite());
        existing.setMaxUsers(organization.getMaxUsers());
        existing.setMaxTemplates(organization.getMaxTemplates());
        existing.setMaxRules(organization.getMaxRules());
        return organizationRepository.save(existing);
    }

    /**
     * 删除组织
     * 根据组织ID删除指定组织
     *
     * @param orgCode 组织代码
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    public void deleteOrganization(String orgCode) {
        findById(orgCode);
        organizationRepository.deleteByOrgCode(orgCode);
    }

    /**
     * 更新组织状态
     * 更新指定组织的状态
     *
     * @param orgCode 组织代码
     * @param status 新状态
     * @return 更新后的组织信息
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    public OrganizationDO updateOrganizationStatus(String orgCode, OrganizationStatusEnum status) {
        OrganizationDO organization = findById(orgCode);
        organization.setStatus(status);
        return organizationRepository.save(organization);
    }

    /**
     * 更新组织设置
     * 更新指定组织的设置信息
     *
     * @param orgCode 组织代码
     * @param organization 包含新设置的组织信息
     * @return 更新后的组织信息
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    public OrganizationDO updateOrganizationSettings(String orgCode, OrganizationDO organization) {
        OrganizationDO existingOrg = findById(orgCode);
        existingOrg.setMaxUsers(organization.getMaxUsers());
        existingOrg.setMaxTemplates(organization.getMaxTemplates());
        existingOrg.setMaxRules(organization.getMaxRules());
        return organizationRepository.save(existingOrg);
    }

    /**
     * 检查组织代码是否存在
     * 用于验证组织代码的唯一性
     *
     * @param orgCode 组织代码
     * @return true 如果组织代码已存在，false 否则
     */
    @Transactional(readOnly = true)
    public boolean existsByOrgCode(String orgCode) {
        return organizationRepository.existsByOrgCode(orgCode);
    }

    /**
     * 根据用户ID查询关联的组织列表
     * 获取指定用户所属的所有组织
     *
     * @param systemUserId 系统用户ID
     * @return 用户关联的组织列表
     */
    public List<OrganizationDO> getOrganizationsByUserId(Long systemUserId) {
        // TODO: Implement user-organization relationship query
        throw new UnsupportedOperationException("Method not implemented yet");
    }
} 