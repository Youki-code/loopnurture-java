package org.springframework.samples.loopnurture.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.users.domain.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.service.OrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 组织服务实现类
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationDO> getOrganizationsByUserId(Long systemUserId) {
        return organizationRepository.findByUserId(systemUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationDO getOrganizationById(String orgId) {
        return organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "orgId", orgId));
    }
} 