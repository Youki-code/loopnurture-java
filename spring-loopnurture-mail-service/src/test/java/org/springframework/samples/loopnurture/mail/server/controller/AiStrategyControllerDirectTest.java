package org.springframework.samples.loopnurture.mail.server.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.enums.AiStrategyTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaAiStrategyMapper;
import org.springframework.samples.loopnurture.mail.infra.po.AiStrategyPO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.GetAiStrategyRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.AiStrategyResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Direct call test for AiStrategyQueryController without HTTP layer.
 */
@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=update","spring.task.scheduling.enabled=false"})
@ActiveProfiles("test")
class AiStrategyControllerDirectTest {

    private static final Logger log = LoggerFactory.getLogger(AiStrategyControllerDirectTest.class);

    @Autowired
    private AiStrategyQueryController aiStrategyQueryController;

    @Autowired
    private JpaAiStrategyMapper aiStrategyMapper;

    @MockBean
    private org.springframework.samples.loopnurture.mail.server.feign.UserServiceClient userServiceClient;

    @jakarta.transaction.Transactional
    @BeforeEach
    void setUp() {
        // Set user context with token to satisfy UserAuthAspect
        UserContext ctx = new UserContext();
        ctx.setUserId("test-user");
        ctx.setOrgCode("TEST_ORG");
        ctx.setToken("test-token");
        UserContext.set(ctx);

        // Bind a mock HttpServletRequest to avoid NoUniqueBeanDefinitionException and to satisfy RequireLoginAspect
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "test-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // Prepare test data – ensure there is at least one enabled strategy for query
        aiStrategyMapper.deleteAll();
        AiStrategyPO po = new AiStrategyPO();
        po.setAiStrategyVersion("v" + UUID.randomUUID().toString().substring(0, 8));
        po.setAiStrategyType(AiStrategyTypeEnum.MARKETING_MAIL.getCode().shortValue());
        po.setAiStrategyContent("{\"template\":\"Hello {{name}}\"}");
        po.setEnableStatus(EnableStatusEnum.ENABLED.getCode().shortValue());
        po.setDeleted(false);
        aiStrategyMapper.save(po);

        // mock token validation
        org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse ok = new org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse();
        ok.setValid(true);
        ok.setUserId("test-user");
        ok.setOrgCode("TEST_ORG");
        org.mockito.Mockito.when(userServiceClient.validateToken(org.mockito.ArgumentMatchers.any())).thenReturn(ApiResponse.ok(ok));
    }

    @Test
    void getLatestStrategy_success() {
        GetAiStrategyRequest req = new GetAiStrategyRequest();
        req.setAiStrategyType(AiStrategyTypeEnum.MARKETING_MAIL.getCode());

        log.info("[TEST] Request => {}", req);
        ApiResponse<AiStrategyResponse> resp = aiStrategyQueryController.getLatestStrategy(req);
        log.info("[TEST] Response => {}", resp);
        Assertions.assertEquals(0, resp.getCode());
        AiStrategyResponse data = resp.getData();
        Assertions.assertNotNull(data);
        Assertions.assertEquals(AiStrategyTypeEnum.MARKETING_MAIL.getCode(), data.getAiStrategyType());
        Assertions.assertNotNull(data.getAiStrategyVersion());
        Assertions.assertFalse(data.getAiStrategyVersion().isEmpty());
        Assertions.assertNotNull(data.getAiStrategyContent());
        log.info("[TEST] Test result: SUCCESS");
    }
} 