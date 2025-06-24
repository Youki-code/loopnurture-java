package org.springframework.samples.loopnurture.mail.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.samples.loopnurture.mail.domain.enums.AiStrategyTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaAiStrategyMapper;
import org.springframework.samples.loopnurture.mail.infra.po.AiStrategyPO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.feign.UserServiceClient;
import org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse;
import org.springframework.samples.loopnurture.mail.server.feign.dto.ValidateTokenRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Integration test for AI Strategy query API.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AiStrategyApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaAiStrategyMapper aiStrategyMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders authHeaders;

    @MockBean
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.set("Authorization", "Bearer test-token");

        // Mock token validation success
        TokenValidationResponse ok = new TokenValidationResponse();
        ok.setValid(true);
        ok.setUserId("test-user");
        ok.setOrgCode("TEST_ORG");
        Mockito.when(userServiceClient.validateToken(ArgumentMatchers.any(ValidateTokenRequest.class)))
               .thenReturn(ApiResponse.ok(ok));
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void getLatestStrategy_success() throws Exception {
        // Build request payload
        String body = objectMapper.writeValueAsString(java.util.Map.of("aiStrategyType", AiStrategyTypeEnum.MARKETING_MAIL.getCode()));
        HttpEntity<String> req = new HttpEntity<>(body, authHeaders);

        ResponseEntity<String> resp = restTemplate.postForEntity(url("/api/v1/ai/strategy/getLatestStrategy"), req, String.class);
        Assertions.assertEquals(HttpStatus.OK, resp.getStatusCode());

        JsonNode root = objectMapper.readTree(resp.getBody());
        Assertions.assertEquals(0, root.path("code").asInt());
        JsonNode data = root.path("data");
        Assertions.assertEquals(AiStrategyTypeEnum.MARKETING_MAIL.getCode().intValue(), data.path("aiStrategyType").asInt());
        Assertions.assertFalse(data.path("aiStrategyVersion").asText().isEmpty());
        Assertions.assertFalse(data.path("aiStrategyContent").asText().isEmpty());
    }
} 