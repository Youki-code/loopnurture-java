package org.springframework.samples.loopnurture.users.infra.repository;

import org.springframework.samples.loopnurture.users.domain.model.UserOrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.UserOrganizationRepository;
import org.springframework.samples.loopnurture.users.infra.converter.UserOrganizationConverter;
import org.springframework.samples.loopnurture.users.infra.mapper.JpaUserOrganizationMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserOrganizationRepositoryImpl implements UserOrganizationRepository {
    
    private final JpaUserOrganizationMapper jpaMapper;
    private final UserOrganizationConverter converter;
    
    public UserOrganizationRepositoryImpl(JpaUserOrganizationMapper jpaMapper, UserOrganizationConverter converter) {
        this.jpaMapper = jpaMapper;
        this.converter = converter;
    }
    
    @Override
    public List<UserOrganizationDO> findBySystemUserId(Long systemUserId) {
        return jpaMapper.findBySystemUserId(systemUserId).stream()
            .map(converter::toDO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<UserOrganizationDO> findByOrgId(String orgId) {
        return jpaMapper.findByOrgId(orgId).stream()
            .map(converter::toDO)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<UserOrganizationDO> findBySystemUserIdAndOrgId(Long systemUserId, String orgId) {
        return jpaMapper.findBySystemUserIdAndOrgId(systemUserId, orgId)
            .map(converter::toDO);
    }
    
    @Override
    public UserOrganizationDO save(UserOrganizationDO userOrganization) {
        return converter.toDO(
            jpaMapper.save(
                converter.toPO(userOrganization)
            )
        );
    }
    
    @Override
    @Transactional
    public void deleteBySystemUserIdAndOrgId(Long systemUserId, String orgId) {
        jpaMapper.deleteBySystemUserIdAndOrgId(systemUserId, orgId);
    }
    
    @Override
    @Transactional
    public void deleteBySystemUserId(Long systemUserId) {
        jpaMapper.deleteBySystemUserId(systemUserId);
    }
    
    @Override
    @Transactional
    public void deleteByOrgId(String orgId) {
        jpaMapper.deleteByOrgId(orgId);
    }
    
    @Override
    public boolean existsBySystemUserIdAndOrgId(Long systemUserId, String orgId) {
        return jpaMapper.existsBySystemUserIdAndOrgId(systemUserId, orgId);
    }
} 