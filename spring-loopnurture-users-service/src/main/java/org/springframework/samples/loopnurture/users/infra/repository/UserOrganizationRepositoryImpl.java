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
    public List<UserOrganizationDO> findByOrgCode(String orgCode) {
        return jpaMapper.findByOrgCode(orgCode).stream()
            .map(converter::toDO)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<UserOrganizationDO> findBySystemUserIdAndOrgCode(Long systemUserId, String orgCode) {
        return jpaMapper.findBySystemUserIdAndOrgCode(systemUserId, orgCode)
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
    public void deleteBySystemUserIdAndOrgCode(Long systemUserId, String orgCode) {
        jpaMapper.softDeleteBySystemUserIdAndOrgCode(systemUserId, orgCode);
    }
    
    @Override
    @Transactional
    public void deleteBySystemUserId(Long systemUserId) {
        jpaMapper.softDeleteBySystemUserId(systemUserId);
    }
    
    @Override
    @Transactional
    public void deleteByOrgCode(String orgCode) {
        jpaMapper.softDeleteByOrgCode(orgCode);
    }
    
    @Override
    public boolean existsBySystemUserIdAndOrgCode(Long systemUserId, String orgCode) {
        return jpaMapper.existsBySystemUserIdAndOrgCode(systemUserId, orgCode);
    }
} 