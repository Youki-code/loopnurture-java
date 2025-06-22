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
            .map(converter::toDO)
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<MarketingUserDO> findById(Long id) {
        return jpaMapper.findById(id)
            .map(converter::toDO);
    }
    
    @Override
    public Optional<MarketingUserDO> findByUserUniq(String userUniq) {
        return jpaMapper.findByUserUniq(userUniq)
            .map(converter::toDO);
    }
    
    @Override
    public Optional<MarketingUserDO> findByPrimaryEmail(String primaryEmail) {
        return jpaMapper.findByPrimaryEmail(primaryEmail)
            .map(converter::toDO);
    }
    
    @Override
    public Optional<MarketingUserDO> findByPhone(String phone) {
        return jpaMapper.findByPhone(phone)
            .map(converter::toDO);
    }
    
    @Override
    public List<MarketingUserDO> findByOrgCode(String orgCode) {
        return jpaMapper.findByCurrentOrgCode(orgCode).stream()
            .map(converter::toDO)
            .collect(Collectors.toList());
    }
    
    @Override
    public MarketingUserDO save(MarketingUserDO user) {
        var po = converter.toPO(user);
        // If this is an update, we need to attach existing ID to prevent JPA from INSERTing again
        if (po.getId() == null && po.getSystemUserId() != null) {
            jpaMapper.findBySystemUserId(po.getSystemUserId()).ifPresent(existing -> po.setId(existing.getId()));
        }
        var savedPo = jpaMapper.save(po);
        return converter.toDO(savedPo);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaMapper.softDeleteById(id);
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

    @Override
    public Optional<MarketingUserDO> findByOAuthInfo(String oauthUserId, Integer authType) {
        return jpaMapper.findByOauthUserIdAndAuthType(oauthUserId, authType.shortValue())
            .map(converter::toDO);
    }

    @Override
    public Optional<MarketingUserDO> findBySystemUserId(Long systemUserId) {
        return jpaMapper.findBySystemUserId(systemUserId)
            .map(converter::toDO);
    }

    @Override
    public void softDeleteBySystemUserId(Long systemUserId) {
        jpaMapper.softDeleteBySystemUserId(systemUserId);
    }
} 