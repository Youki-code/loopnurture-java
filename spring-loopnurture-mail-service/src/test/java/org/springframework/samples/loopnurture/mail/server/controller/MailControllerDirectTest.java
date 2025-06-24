package org.springframework.samples.loopnurture.mail.server.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.server.controller.dto.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

/**
 * Direct controller flow test for marketing template & send rule APIs.
 */
@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=update","spring.task.scheduling.enabled=false"})
@ActiveProfiles("test")
class MailControllerDirectTest {

    @Autowired
    private MarketingMailTemplateOperateController templateOperateController;
    @Autowired
    private EmailSendRuleOperateController ruleOperateController;
    @Autowired
    private MarketingEmailTemplateRepository templateRepository;
    @Autowired
    private EmailSendRuleRepository ruleRepository;

    @MockBean
    private org.springframework.samples.loopnurture.mail.server.feign.UserServiceClient userServiceClient;

    private static final Logger log = LoggerFactory.getLogger(MailControllerDirectTest.class);

    @BeforeEach
    void initContext() {
        UserContext ctx = new UserContext();
        ctx.setUserId("test-user");
        ctx.setOrgCode("TEST_ORG");
        ctx.setToken("test-token");
        UserContext.set(ctx);
        // Bind mock request
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "test-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // mock token validation
        org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse ok = new org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse();
        ok.setValid(true);
        ok.setUserId(ctx.getUserId());
        ok.setOrgCode(ctx.getOrgCode());
        org.mockito.Mockito.when(userServiceClient.validateToken(org.mockito.ArgumentMatchers.any())).thenReturn(ApiResponse.ok(ok));

        templateRepository.deleteByOrgCode(ctx.getOrgCode());
        ruleRepository.deleteByOrgCode(ctx.getOrgCode());
    }

    @Test
    void fullFlow_viaControllers() {
        String uniq = UUID.randomUUID().toString().substring(0, 8);
        // 1. create template
        CreateMarketingEmailTemplateRequest cReq = new CreateMarketingEmailTemplateRequest();
        cReq.setTemplateName("unit_tpl_" + uniq);
        cReq.setContentType(ContentTypeEnum.TEXT.getCode());
        cReq.setContentTemplate("Hello {{name}}");
        cReq.setInputContent("{\"name\":\"Bob\"}");
        cReq.setAiStrategyVersion("v1");
        ApiResponse<MarketingEmailTemplateResponse> createTplResp = templateOperateController.createTemplate(cReq);
        String templateId = createTplResp.getData().getTemplateId();
        Assertions.assertNotNull(templateId);

        // 2. modify template
        ModifyMarketingEmailTemplateRequest mReq = new ModifyMarketingEmailTemplateRequest();
        mReq.setTemplateId(templateId);
        mReq.setTemplateName("unit_tpl_mod_" + uniq);
        mReq.setContentType(ContentTypeEnum.HTML.getCode());
        mReq.setContentTemplate("<h1>Hello {{name}}</h1>");
        mReq.setInputContent("{\"name\":\"Alice\"}");
        mReq.setEnableStatus(EnableStatusEnum.ENABLED.getCode());
        mReq.setAiStrategyVersion("v2");
        ApiResponse<MarketingEmailTemplateResponse> modifyResp = templateOperateController.modifyTemplate(mReq);
        MarketingEmailTemplateResponse tplDetail = modifyResp.getData();
        log.info("[TEST] Template after modification => {}", tplDetail);
        Assertions.assertNotNull(tplDetail, "Template should exist after modification");
        Assertions.assertEquals(mReq.getTemplateName(), tplDetail.getTemplateName());

        // 3. create rule
        CreateEmailSendRuleRequest rReq = new CreateEmailSendRuleRequest();
        rReq.setRuleName("unit_rule_" + uniq);
        rReq.setTemplateId(templateId);
        rReq.setRuleType(1);
        rReq.setRecipients(List.of("bob@example.com"));
        ApiResponse<CreateEmailSendRuleResponse> ruleCreateResp = ruleOperateController.createRule(rReq);
        String ruleId = ruleCreateResp.getData().getRuleId();
        Assertions.assertNotNull(ruleId);

        // 4. modify rule
        UpdateEmailSendRuleRequest rMod = new UpdateEmailSendRuleRequest();
        rMod.setRuleId(ruleId);
        rMod.setRuleName("unit_rule_mod_" + uniq);
        rMod.setTemplateId(templateId);
        rMod.setRuleType(1);
        rMod.setRecipients(List.of("alice@example.com"));
        rMod.setEnableStatus(EnableStatusEnum.ENABLED.getCode());
        ruleOperateController.modifyRule(rMod);
        EmailSendRuleDO ruleDetail = ruleRepository.findByRuleId(ruleId);
        Assertions.assertEquals(rMod.getRuleName(), ruleDetail.getRuleName());

        // 5. execute rule
        ExecuteEmailSendRuleRequest exec = new ExecuteEmailSendRuleRequest();
        exec.setRuleId(ruleId);
        ruleOperateController.executeRule(exec);
        EmailSendRuleDO executed = ruleRepository.findByRuleId(ruleId);
        Assertions.assertTrue(executed.getExecutionCount() >= 1);

        // 6. delete rule
        DeleteEmailSendRuleRequest delRule = new DeleteEmailSendRuleRequest();
        delRule.setRuleId(ruleId);
        ruleOperateController.deleteRule(delRule);
        Assertions.assertNull(ruleRepository.findByRuleId(ruleId));

        // 7. delete template
        TemplateIdRequest delTpl = new TemplateIdRequest();
        delTpl.setTemplateId(templateId);
        templateOperateController.deleteTemplate(delTpl);
        Assertions.assertNull(templateRepository.getByTemplateId(templateId));

        log.info("[TEST] fullFlow_viaControllers finished SUCCESS for uniq id {}", uniq);
    }
}
