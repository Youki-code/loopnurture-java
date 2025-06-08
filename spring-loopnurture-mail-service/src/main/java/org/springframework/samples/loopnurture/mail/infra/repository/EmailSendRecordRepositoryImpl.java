package org.springframework.samples.loopnurture.mail.infra.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRecordRepository;
import org.springframework.samples.loopnurture.mail.infra.converter.EmailSendRecordConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaEmailSendRecordMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 邮件发送记录仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class EmailSendRecordRepositoryImpl implements EmailSendRecordRepository {

    private final JpaEmailSendRecordMapper jpaMapper;
    private final EmailSendRecordConverter converter;

    @Override
    public EmailSendRecordDO save(EmailSendRecordDO record) {
        var po = converter.toPO(record);
        po = jpaMapper.save(po);
        return converter.toDO(po);
    }

    @Override
    public Optional<EmailSendRecordDO> findById(String id) {
        return jpaMapper.findById(id)
            .map(converter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> findByOrgId(String orgId, Pageable pageable) {
        return jpaMapper.findByOrgIdOrderByCreatedAtDesc(orgId, pageable)
            .map(converter::toDO);
    }

    @Override
    public Page<EmailSendRecordDO> findByTemplateCode(String templateCode, Pageable pageable) {
        return jpaMapper.findByTemplateCodeOrderByCreatedAtDesc(templateCode, pageable)
            .map(converter::toDO);
    }

    @Override
    public List<EmailSendRecordDO> findLogsForRetry(int maxRetries, LocalDateTime now) {
        return jpaMapper.findLogsForRetry(maxRetries, now)
            .stream()
            .map(converter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public long countByOrgIdAndStatusAndSendTimeBetween(String orgId, Integer status,
                                                       LocalDateTime start, LocalDateTime end) {
        return jpaMapper.countByOrgIdAndStatusAndSentAtBetween(orgId, status, start, end);
    }
} 