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
import java.util.ArrayList;

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
        emailSendRecordMapper.save(po);
        return emailSendRecordConverter.toDO(po);
    }

    @Override
    public EmailSendRecordDO findById(Long id) {
        return emailSendRecordMapper.findById(id)
                .map(emailSendRecordConverter::toDO)
                .orElse(null);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgCodeAndStatus(String orgCode, EmailStatusEnum status, Pageable pageable) {
        return emailSendRecordMapper.findByOrgCodeAndStatus(orgCode, status != null ? status.getCode().shortValue() : null, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgCodeAndSentAtBetween(String orgCode, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return emailSendRecordMapper.findByOrgCodeAndSendTimeBetween(orgCode, startTime, endTime, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable) {
        return emailSendRecordMapper.findByOrgCodeAndTemplateId(orgCode, templateId, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public List<EmailSendRecordDO> findRetryableRecords(int maxRetries) {
        // 查找状态为FAILED且重试次数小于最大重试次数的记录
        return emailSendRecordMapper.findByStatus(EmailStatusEnum.FAILED.getCode().shortValue())
                .stream()
                .map(emailSendRecordConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public long countByOrgCodeAndStatus(String orgCode, EmailStatusEnum status) {
        return emailSendRecordMapper.countByOrgCodeAndStatus(orgCode, status != null ? status.getCode().shortValue() : null);
    }

    @Override
    public void update(Long id, EmailSendRecordDO record) {
        var po = emailSendRecordConverter.toPO(record);
        po.setId(id);
        emailSendRecordMapper.save(po);
    }

    @Override
    public void deleteById(Long id) {
        emailSendRecordMapper.softDeleteById(id);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgCode(String orgCode, Pageable pageable) {
        return emailSendRecordMapper.findByOrgCode(orgCode, pageable)
                .map(emailSendRecordConverter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgCodeAndStatusAndSentAtBetween(String orgCode, EmailStatusEnum status, 
                                                                        LocalDateTime startTime, LocalDateTime endTime, 
                                                                        Pageable pageable) {
        // 先按组织编码和状态查询，然后在内存中过滤时间范围
        Page<EmailSendRecordDO> records = findByOrgCodeAndStatus(orgCode, status, pageable);
        
        List<EmailSendRecordDO> filteredRecords = records.getContent().stream()
                .filter(record -> {
                    if (record.getSentAt() == null) return false;
                    LocalDateTime sentAt = record.getSentAt().toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDateTime();
                    return !sentAt.isBefore(startTime) && !sentAt.isAfter(endTime);
                })
                .collect(Collectors.toList());
        
        return new org.springframework.data.domain.PageImpl<>(filteredRecords, pageable, filteredRecords.size());
    }

    @Override
    public Page<EmailSendRecordDO> findByTemplateId(String templateId, Pageable pageable) {
        // 由于Mapper中没有这个方法，暂时返回空页面
        return new org.springframework.data.domain.PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    @Override
    public EmailSendRecordDO findByRecordId(String recordId) {
        try {
            Long id = Long.parseLong(recordId);
            return findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }
} 