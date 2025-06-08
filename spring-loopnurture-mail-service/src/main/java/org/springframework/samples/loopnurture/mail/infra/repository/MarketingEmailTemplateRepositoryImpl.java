package org.springframework.samples.loopnurture.mail.infra.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailTemplateStatusEnum;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaMarketingEmailTemplateMapper;
import org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 营销邮件模板仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class MarketingEmailTemplateRepositoryImpl implements MarketingEmailTemplateRepository {

    private final JpaMarketingEmailTemplateMapper jpaMapper;
    private final MarketingEmailTemplateConverter converter;

    @Override
    public MarketingEmailTemplateDO save(MarketingEmailTemplateDO template) {
        var po = converter.toPO(template);
        po = jpaMapper.save(po);
        return converter.toDO(po);
    }

    @Override
    public Optional<MarketingEmailTemplateDO> findById(Long id) {
        return jpaMapper.findById(id)
            .map(converter::toDO);
    }

    @Override
    public Optional<MarketingEmailTemplateDO> findByOrgIdAndTemplateCode(Long orgId, String templateCode) {
        return jpaMapper.findByOrgIdAndTemplateCode(orgId, templateCode)
            .map(converter::toDO);
    }

    @Override
    public List<MarketingEmailTemplateDO> findActiveTemplatesByOrgId(Long orgId) {
        return jpaMapper.findActiveTemplatesByOrgId(orgId).stream()
            .map(converter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByOrgId(Long orgId, Pageable pageable) {
        return jpaMapper.findByOrgId(orgId, pageable)
            .map(converter::toDO);
    }

    @Override
    public boolean existsByOrgIdAndTemplateCode(Long orgId, String templateCode) {
        return jpaMapper.existsByOrgIdAndTemplateCode(orgId, templateCode);
    }

    @Override
    public void deleteById(Long id) {
        jpaMapper.deleteById(id);
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByOrgIdAndStatus(Long orgId, EmailTemplateStatusEnum status, Pageable pageable) {
        return jpaMapper.findByOrgIdAndStatus(orgId, status.getCode(), pageable)
            .map(converter::toDO);
    }

    @Override
    public List<MarketingEmailTemplateDO> findByOrgIdAndStatus(Long orgId, EmailTemplateStatusEnum status) {
        return jpaMapper.findByOrgIdAndStatus(orgId, status.getCode()).stream()
            .map(converter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public long countByOrgIdAndStatus(Long orgId, EmailTemplateStatusEnum status) {
        return jpaMapper.countByOrgIdAndStatus(orgId, status.getCode());
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByExample(MarketingEmailTemplateDO example, Pageable pageable) {
        MarketingEmailTemplatePO po = converter.toPO(example);
        return jpaMapper.findAll(Example.of(po), pageable)
            .map(converter::toDO);
    }
} 