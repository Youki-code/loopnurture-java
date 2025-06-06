package org.springframework.samples.loopnurture.users.infra.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.domain.vo.OrganizationSettingsVO;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import org.springframework.samples.loopnurture.users.infra.repository.JpaOrganizationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组织仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {

    private final JpaOrganizationRepository jpaOrganizationRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<OrganizationDO> findByUserId(Long systemUserId) {
        return jpaOrganizationRepository.findBySystemUserId(systemUserId)
                .stream()
                .map(this::convertToDO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrganizationDO> findById(String orgId) {
        return jpaOrganizationRepository.findById(orgId)
                .map(this::convertToDO);
    }

    @Override
    public List<OrganizationDO> findAll() {
        return jpaOrganizationRepository.findAll()
                .stream()
                .map(this::convertToDO)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationDO save(OrganizationDO organization) {
        OrganizationPO po = convertToPO(organization);
        return convertToDO(jpaOrganizationRepository.save(po));
    }

    @Override
    public void deleteById(String orgId) {
        jpaOrganizationRepository.deleteById(orgId);
    }

    private OrganizationDO convertToDO(OrganizationPO po) {
        OrganizationDO organization = new OrganizationDO();
        organization.setOrgId(po.getOrgId());
        organization.setOrgName(po.getOrgName());
        organization.setDescription(po.getDescription());
        organization.setStatus(po.getStatus());
        organization.setOrgType(po.getOrgType());
        try {
            organization.setSettings(objectMapper.readValue(po.getSettings(), OrganizationSettingsVO.class));
        } catch (Exception e) {
            organization.setSettings(new OrganizationSettingsVO());
        }
        organization.setCreatedAt(po.getCreatedAt());
        organization.setUpdatedAt(po.getUpdatedAt());
        organization.setCreatedBy(po.getCreatedBy());
        organization.setUpdatedBy(po.getUpdatedBy());
        return organization;
    }

    private OrganizationPO convertToPO(OrganizationDO organization) {
        OrganizationPO po = new OrganizationPO();
        po.setOrgId(organization.getOrgId());
        po.setOrgName(organization.getOrgName());
        po.setDescription(organization.getDescription());
        po.setStatus(organization.getStatus());
        po.setOrgType(organization.getOrgType());
        try {
            po.setSettings(objectMapper.writeValueAsString(organization.getSettings()));
        } catch (Exception e) {
            po.setSettings("{}");
        }
        po.setCreatedAt(organization.getCreatedAt());
        po.setUpdatedAt(organization.getUpdatedAt());
        po.setCreatedBy(organization.getCreatedBy());
        po.setUpdatedBy(organization.getUpdatedBy());
        return po;
    }
} 