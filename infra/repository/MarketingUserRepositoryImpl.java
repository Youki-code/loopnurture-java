package org.springframework.samples.loopnurture.users.infra.repository;

import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.domain.repository.MarketingUserRepository;
import org.springframework.samples.loopnurture.users.infra.converter.MarketingUserConverter;
import org.springframework.samples.loopnurture.users.infra.mapper.JpaMarketingUserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MarketingUserRepositoryImpl implements MarketingUserRepository {
    
    private final JpaMarketingUserMapper jpaMapper;
    private final MarketingUserConverter converter;
    
    public MarketingUserRepositoryImpl(JpaMarketingUserMapper jpaMapper, MarketingUserConverter converter) {
        this.jpaMapper = jpaMapper;
        this.converter = converter;
    }
    
    @Override
    public List<MarketingUserDO> findAll() {
        return jpaMapper.findAll().stream()
            .map(converter::toEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<MarketingUserDO> findById(String id) {
        return jpaMapper.findById(id)
            .map(converter::toEntity);
    }
    
    @Override
    public Optional<MarketingUserDO> findByUserUniq(String userUniq) {
        return jpaMapper.findByUserUniq(userUniq)
            .map(converter::toEntity);
    }
    
    @Override
    public Optional<MarketingUserDO> findByPrimaryEmail(String primaryEmail) {
        return jpaMapper.findByPrimaryEmail(primaryEmail)
            .map(converter::toEntity);
    }
    
    @Override
    public Optional<MarketingUserDO> findByPhone(String phone) {
        return jpaMapper.findByPhone(phone)
            .map(converter::toEntity);
    }
    
    @Override
    public List<MarketingUserDO> findByOrgId(String orgId) {
        return jpaMapper.findByOrgId(orgId).stream()
            .map(converter::toEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public MarketingUserDO save(MarketingUserDO user) {
        return converter.toEntity(
            jpaMapper.save(
                converter.toPO(user)
            )
        );
    }
    
    @Override
    public void deleteById(String id) {
        jpaMapper.deleteById(id);
    }
    
    @Override
    public boolean existsByUserUniq(String userUniq) {
        return jpaMapper.existsByUserUniq(userUniq);
    }
    
    @Override
    public boolean existsByPrimaryEmail(String primaryEmail) {
        return jpaMapper.existsByPrimaryEmail(primaryEmail);
    }
    
    @Override
    public boolean existsByPhone(String phone) {
        return jpaMapper.existsByPhone(phone);
    }
} 