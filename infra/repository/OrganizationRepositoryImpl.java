package org.springframework.samples.loopnurture.users.infra.repository;

import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.infra.converter.OrganizationConverter;
import org.springframework.samples.loopnurture.users.infra.mapper.JpaOrganizationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {
    
    private final JpaOrganizationMapper jpaMapper;
    private final OrganizationConverter converter;
    
    public OrganizationRepositoryImpl(JpaOrganizationMapper jpaMapper, OrganizationConverter converter) {
        this.jpaMapper = jpaMapper;
        this.converter = converter;
    }
    
    @Override
    public List<OrganizationDO> findAll() {
        return jpaMapper.findAll().stream()
            .map(converter::toEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<OrganizationDO> findById(String id) {
        return jpaMapper.findById(id)
            .map(converter::toEntity);
    }
    
    @Override
    public Optional<OrganizationDO> findByOrgCode(String orgCode) {
        return Optional.ofNullable(jpaMapper.findByOrgCode(orgCode))
            .map(converter::toEntity);
    }
    
    @Override
    public OrganizationDO save(OrganizationDO organization) {
        return converter.toEntity(
            jpaMapper.save(
                converter.toPO(organization)
            )
        );
    }
    
    @Override
    public void deleteById(String id) {
        jpaMapper.deleteById(id);
    }
    
    @Override
    public boolean existsByOrgCode(String orgCode) {
        return jpaMapper.existsByOrgCode(orgCode);
    }
} 