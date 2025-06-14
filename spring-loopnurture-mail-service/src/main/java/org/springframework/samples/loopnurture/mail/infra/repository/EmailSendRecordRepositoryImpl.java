package org.springframework.samples.loopnurture.mail.infra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRecordRepository;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatusEnum;
import org.springframework.samples.loopnurture.mail.infra.converter.EmailSendRecordConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaEmailSendRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.loopnurture.mail.domain.repository.dto.EmailSendRecordPageQueryDTO;

/**
 * 邮件发送记录仓储实现类
 * 负责处理持久化相关的逻辑，包括ID的生成和管理
 */
@Repository
public class EmailSendRecordRepositoryImpl implements EmailSendRecordRepository {

    private final JpaEmailSendRecordMapper emailSendRecordMapper;
    private final EmailSendRecordConverter emailSendRecordConverter;

    @Autowired
    public EmailSendRecordRepositoryImpl(JpaEmailSendRecordMapper emailSendRecordMapper,
                                       EmailSendRecordConverter emailSendRecordConverter) {
        this.emailSendRecordMapper = emailSendRecordMapper;
        this.emailSendRecordConverter = emailSendRecordConverter;
    }

    @Override
    public EmailSendRecordDO save(EmailSendRecordDO record) {
        var po = emailSendRecordConverter.toPO(record);
        if (po.getId() == null) {
            po.setId(UUID.randomUUID().toString());
        }
        emailSendRecordMapper.save(po);
        return emailSendRecordConverter.toDO(po);
    }

    @Override
    public EmailSendRecordDO findById(String id) {
        return emailSendRecordMapper.findById(id)
                .map(emailSendRecordConverter::toDO)
                .orElse(null);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgCodeAndStatus(String orgCode, EmailStatusEnum status, Pageable pageable) {
        return emailSendRecordMapper.findByOrgCodeAndStatus(orgCode, status != null ? status.getCode() : null, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgCodeAndSentAtBetween(String orgCode, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return emailSendRecordMapper.findByOrgCodeAndSentAtBetween(orgCode, startTime, endTime, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable) {
        return emailSendRecordMapper.findByOrgCodeAndTemplateId(orgCode, templateId, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public List<EmailSendRecordDO> findRetryableRecords(int maxRetries) {
        return emailSendRecordMapper.findByStatusAndRetryCountLessThan(EmailStatusEnum.FAILED.getCode(), maxRetries)
                .stream()
                .map(emailSendRecordConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public long countByOrgCodeAndStatus(String orgCode, EmailStatusEnum status) {
        return emailSendRecordMapper.countByOrgCodeAndStatus(orgCode, status != null ? status.getCode() : null);
    }

    @Override
    public long countByOrgIdAndStatusAndSendTimeBetween(String orgId, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        return emailSendRecordMapper.countByOrgIdAndStatusAndSentAtBetween(orgId, status, startTime, endTime);
    }

    @Override
    public void update(String id, EmailSendRecordDO record) {
        var po = emailSendRecordConverter.toPO(record, id);
        emailSendRecordMapper.save(po);
    }

    @Override
    public List<EmailSendRecordDO> findRecordsForRetry(int maxRetries, LocalDateTime beforeTime) {
        return emailSendRecordMapper.findByStatusAndRetryCountLessThanAndCreatedAtBefore(
                EmailStatusEnum.FAILED.getCode(),
                maxRetries,
                beforeTime
            ).stream()
            .map(emailSendRecordConverter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public Page<EmailSendRecordDO> findByTemplateId(String templateId, Pageable pageable) {
        return emailSendRecordMapper.findByTemplateId(templateId, pageable)
            .map(emailSendRecordConverter::toDO);
    }

    @Override
    public long countByOrgIdAndStatusAndSentAtBetween(String orgId, Integer status, LocalDateTime startTime, LocalDateTime endTime) {
        return emailSendRecordMapper.countByOrgIdAndStatusAndSentAtBetween(orgId, status, startTime, endTime);
    }

    @Override
    public List<EmailSendRecordDO> findByStatusAndRetryCountLessThanAndCreatedAtBefore(Integer status, int maxRetries, LocalDateTime beforeTime) {
        return emailSendRecordMapper.findByStatusAndRetryCountLessThanAndCreatedAtBefore(status, maxRetries, beforeTime)
            .stream()
            .map(emailSendRecordConverter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public Page<EmailSendRecordDO> findByRuleId(String ruleId, Pageable pageable) {
        return emailSendRecordMapper.findByRuleId(ruleId, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgId(String orgId, Pageable pageable) {
        return emailSendRecordMapper.findByOrgCode(orgId, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> pageQuery(EmailSendRecordPageQueryDTO query) {
        Pageable pageable = PageRequest.of(query.getPageNum(), query.getPageSize());
        return emailSendRecordMapper.findAll(pageable).map(emailSendRecordConverter::toDO);
    }
} 