package org.springframework.samples.loopnurture.mail.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.samples.loopnurture.mail.domain.enums.ContentType;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatus;
import org.springframework.samples.loopnurture.mail.domain.enums.TemplateType;
import org.springframework.samples.loopnurture.mail.domain.model.EmailTemplate;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecord;
import org.springframework.samples.loopnurture.mail.service.EmailService;
import org.springframework.samples.loopnurture.mail.server.context.UserContext;
import org.springframework.samples.loopnurture.mail.server.controller.dto.SendEmailRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserContext userContext;

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
    void createTemplate() throws Exception {
        when(emailService.createTemplate(any(EmailTemplate.class))).thenReturn(template);

        mockMvc.perform(post("/api/v1/emails/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(template)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(template.getId()))
                .andExpect(jsonPath("$.templateCode").value(template.getTemplateCode()));
    }

    @Test
    void updateTemplate() throws Exception {
        when(emailService.updateTemplate(eq(template.getId()), any(EmailTemplate.class))).thenReturn(template);

        mockMvc.perform(put("/api/v1/emails/templates/" + template.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(template)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(template.getId()));
    }

    @Test
    void getTemplate() throws Exception {
        when(emailService.getTemplate(template.getId())).thenReturn(template);

        mockMvc.perform(get("/api/v1/emails/templates/" + template.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(template.getId()));
    }

    @Test
    void getTemplates() throws Exception {
        when(emailService.getOrgTemplates(eq(orgId), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(template)));

        mockMvc.perform(get("/api/v1/emails/templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(template.getId()));
    }

    @Test
    void sendEmail() throws Exception {
        SendEmailRequest request = new SendEmailRequest();
        request.setTemplateCode(template.getTemplateCode());
        request.setRecipient("test@example.com");
        request.setRecipientName("Test User");
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "Test User");
        request.setVariables(variables);

        when(emailService.sendEmail(
                eq(request.getTemplateCode()),
                eq(request.getRecipient()),
                eq(request.getRecipientName()),
                eq(request.getVariables())
        )).thenReturn(record);

        mockMvc.perform(post("/api/v1/emails/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(record.getId()));
    }

    @Test
    void getSendRecords() throws Exception {
        when(emailService.getSendRecords(eq(orgId), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(record)));

        mockMvc.perform(get("/api/v1/emails/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(record.getId()));
    }
} 