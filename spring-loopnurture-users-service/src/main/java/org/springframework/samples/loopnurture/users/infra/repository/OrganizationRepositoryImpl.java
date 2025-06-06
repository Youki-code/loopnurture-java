package org.springframework.samples.loopnurture.users.infra.repository;

import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.infra.converter.OrganizationConverter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {
    
    private final JpaOrganizationRepository jpaRepository;
    private final OrganizationConverter converter;
    
    public OrganizationRepositoryImpl(JpaOrganizationRepository jpaRepository, OrganizationConverter converter) {
        this.jpaRepository = jpaRepository;
        this.converter = converter;
    }
    
    @Override
    public List<OrganizationDO> findAll() {
        return jpaRepository.findAll().stream()
            .map(converter::toEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<OrganizationDO> findById(String id) {
        return jpaRepository.findById(id)
            .map(converter::toEntity);
    }
    
    @Override
    public Optional<OrganizationDO> findByOrgCode(String orgCode) {
        return Optional.ofNullable(jpaRepository.findByOrgCode(orgCode))
            .map(converter::toEntity);
    }
    
    @Override
    public OrganizationDO save(OrganizationDO organization) {
        return converter.toEntity(
            jpaRepository.save(
                converter.toPO(organization)
            )
        );
    }
    
    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByOrgCode(String orgCode) {
        return jpaRepository.existsByOrgCode(orgCode);
    }
} 