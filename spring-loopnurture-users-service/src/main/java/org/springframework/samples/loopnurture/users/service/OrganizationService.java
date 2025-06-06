package org.springframework.samples.loopnurture.users.service;

import org.springframework.samples.loopnurture.users.domain.model.Organization;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;

/**
 * 组织服务接口
 */
public interface OrganizationService {
    
    /**
     * 根据用户ID查询关联的组织列表
     *
     * @param systemUserId 系统用户ID
     * @return 组织列表
     */
    List<OrganizationDO> getOrganizationsByUserId(Long systemUserId);

    /**
     * 根据组织ID查询组织详情
     *
     * @param orgId 组织ID
     * @return 组织详情
     */
    OrganizationDO getOrganizationById(String orgId);
}

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional(readOnly = true)
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Organization> findById(String id) {
        return organizationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Organization> findByOrgCode(String orgCode) {
        return organizationRepository.findByOrgCode(orgCode);
    }

    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    public void deleteById(String id) {
        organizationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByOrgCode(String orgCode) {
        return organizationRepository.existsByOrgCode(orgCode);
    }

    @Override
    public List<OrganizationDO> getOrganizationsByUserId(Long systemUserId) {
        // Implementation needed
        throw new UnsupportedOperationException("Method not implemented");
    }

    @Override
    public OrganizationDO getOrganizationById(String orgId) {
        // Implementation needed
        throw new UnsupportedOperationException("Method not implemented");
    }
} 