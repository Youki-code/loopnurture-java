package org.springframework.samples.loopnurture.users.service;

import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.model.vo.OrganizationSettingsVO;
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
        return organizationRepository.findById(id)
            .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id: " + id));
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
        return organizationRepository.findByOrgCode(orgCode)
            .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with code: " + orgCode));
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
     * @param id 组织ID
     * @param organization 组织信息
     * @return 更新后的组织信息
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    public OrganizationDO updateOrganization(String id, OrganizationDO organization) {
        findById(id); // 检查组织是否存在
        organization.setId(id);
        return organizationRepository.save(organization);
    }

    /**
     * 删除组织
     * 根据组织ID删除指定组织
     *
     * @param id 组织ID
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    public void deleteOrganization(String id) {
        findById(id); // 检查组织是否存在
        organizationRepository.deleteById(id);
    }

    /**
     * 更新组织状态
     * 更新指定组织的状态
     *
     * @param id 组织ID
     * @param status 新状态
     * @return 更新后的组织信息
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    public OrganizationDO updateOrganizationStatus(String id, OrganizationStatusEnum status) {
        OrganizationDO organization = findById(id);
        organization.setStatus(status);
        return organizationRepository.save(organization);
    }

    /**
     * 更新组织设置
     * 更新指定组织的设置信息
     *
     * @param id 组织ID
     * @param organization 包含新设置的组织信息
     * @return 更新后的组织信息
     * @throws OrganizationNotFoundException 如果组织不存在
     */
    public OrganizationDO updateOrganizationSettings(String id, OrganizationDO organization) {
        OrganizationDO existingOrg = findById(id);
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