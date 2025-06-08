package org.springframework.samples.loopnurture.mail.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.loopnurture.mail.domain.enums.ContentType;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatus;
import org.springframework.samples.loopnurture.mail.domain.enums.TemplateType;
import org.springframework.samples.loopnurture.mail.domain.model.EmailTemplate;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecord;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRecordRepository;
import org.springframework.samples.loopnurture.mail.service.impl.EmailServiceImpl;
import org.springframework.samples.loopnurture.mail.server.context.UserContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private EmailTemplateRepository templateRepository;

    @Mock
    private EmailSendRecordRepository sendRecordRepository;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private EmailServiceImpl emailService;

    private EmailTemplate template;
    private EmailSendRecord record;
    private final String orgId = "test-org";
    private final String userId = "test-user";

    @BeforeEach
    void setUp() {
        template = new EmailTemplate();
        template.setId("test-template");
        template.setOrgId(orgId);
        template.setTemplateCode("TEST_TPL");
        template.setTemplateName("Test Template");
        template.setTemplateType(TemplateType.CUSTOM.getCode());
        template.setContentType(ContentType.HTML.getCode());
        template.setSubjectTemplate("Test Subject");
        template.setContentTemplate("Test Content");
        template.setIsActive(true);

        record = new EmailSendRecord();
        record.setId("test-record");
        record.setOrgId(orgId);
        record.setTemplateId(template.getId());
        record.setStatusEnum(EmailStatus.PENDING);

        when(userContext.getOrgId()).thenReturn(orgId);
        when(userContext.getUserId()).thenReturn(userId);
    }

    @Test
    void createTemplate() {
        when(templateRepository.existsByOrgIdAndTemplateCode(orgId, template.getTemplateCode())).thenReturn(false);
        when(templateRepository.save(any(EmailTemplate.class))).thenReturn(template);

        EmailTemplate created = emailService.createTemplate(template);

        assertNotNull(created);
        assertEquals(template.getTemplateCode(), created.getTemplateCode());
        assertEquals(userId, created.getCreatedBy());
        verify(templateRepository).save(any(EmailTemplate.class));
    }

    @Test
    void updateTemplate() {
        when(templateRepository.findById(template.getId())).thenReturn(Optional.of(template));
        when(templateRepository.save(any(EmailTemplate.class))).thenReturn(template);

        EmailTemplate updated = emailService.updateTemplate(template.getId(), template);

        assertNotNull(updated);
        assertEquals(template.getId(), updated.getId());
        assertEquals(userId, updated.getUpdatedBy());
        verify(templateRepository).save(any(EmailTemplate.class));
    }

    @Test
    void sendEmail() throws Exception {
        String recipient = "test@example.com";
        String recipientName = "Test User";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", recipientName);

        when(templateRepository.findByOrgIdAndTemplateCode(orgId, template.getTemplateCode()))
            .thenReturn(Optional.of(template));
        when(templateEngine.process(anyString(), any(Context.class)))
            .thenReturn("Processed Subject")
            .thenReturn("Processed Content");
        when(objectMapper.writeValueAsString(variables)).thenReturn("{}");
        when(sendRecordRepository.save(any(EmailSendRecord.class))).thenReturn(record);

        EmailSendRecord sent = emailService.sendEmail(template.getTemplateCode(), recipient, recipientName, variables);

        assertNotNull(sent);
        assertEquals(EmailStatus.PENDING.getCode(), sent.getStatus());
        verify(sendRecordRepository).save(any(EmailSendRecord.class));
    }

    @Test
    void getSendRecords() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<EmailSendRecord> page = new PageImpl<>(Collections.singletonList(record));

        when(sendRecordRepository.findByOrgIdOrderByCreatedAtDesc(orgId, pageable)).thenReturn(page);

        Page<EmailSendRecord> records = emailService.getSendRecords(orgId, pageable);

        assertNotNull(records);
        assertEquals(1, records.getTotalElements());
        verify(sendRecordRepository).findByOrgIdOrderByCreatedAtDesc(orgId, pageable);
    }
} 