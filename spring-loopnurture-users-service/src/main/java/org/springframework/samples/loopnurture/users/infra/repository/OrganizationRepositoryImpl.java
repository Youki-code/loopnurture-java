package org.springframework.samples.loopnurture.users.infra.repository;

import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.infra.mapper.JpaOrganizationMapper;
import org.springframework.samples.loopnurture.users.infra.converter.OrganizationConverter;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组织仓储实现类
 */
@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {
    
    private final JpaOrganizationMapper jpaOrganizationMapper;
    private final OrganizationConverter organizationConverter;

    public OrganizationRepositoryImpl(JpaOrganizationMapper jpaOrganizationMapper, 
                                    OrganizationConverter organizationConverter) {
        this.jpaOrganizationMapper = jpaOrganizationMapper;
        this.organizationConverter = organizationConverter;
    }

    @Override
    public List<OrganizationDO> findByUserId(Long systemUserId) {
        List<OrganizationPO> pos = jpaOrganizationMapper.findBySystemUserId(systemUserId);
        return pos.stream()
            .map(organizationConverter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public List<OrganizationDO> findAll() {
        List<OrganizationPO> pos = jpaOrganizationMapper.findAll();
        return pos.stream()
            .map(organizationConverter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public OrganizationDO save(OrganizationDO organization) {
        OrganizationPO po = organizationConverter.toPO(organization);
        // 如果存在相同orgCode则沿用其id实现更新
        jpaOrganizationMapper.findByOrgCode(po.getOrgCode()).ifPresent(existing -> po.setId(existing.getId()));
        OrganizationPO savedPo = jpaOrganizationMapper.save(po);
        return organizationConverter.toDO(savedPo);
    }

    @Override
    public void deleteByOrgCode(String orgCode) {
        jpaOrganizationMapper.softDeleteByOrgCode(orgCode);
    }

    @Override
    public OrganizationDO findByOrgCode(String orgCode) {
        return jpaOrganizationMapper.findByOrgCode(orgCode)
            .map(organizationConverter::toDO)
            .orElse(null);
    }

    @Override
    public boolean existsByOrgCode(String orgCode) {
        return jpaOrganizationMapper.existsByOrgCode(orgCode);
    }
} 