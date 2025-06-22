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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.samples.loopnurture.mail.server.feign.UserServiceClient;
import org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse;
import org.springframework.samples.loopnurture.mail.server.feign.dto.ValidateTokenRequest;

import java.util.*;

/**
 * Full-stack integration tests for the Mail-Service REST API defined in docs/api.md.
 *
 * Flow covered:
 *   1. Create marketing email template → modify → getDetail → pageTemplates
 *   2. Create email send rule  → modify → getRuleDetail → pageRules → executeRule → deleteRule
 *   3. Finally delete marketing template
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MailApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders authHeaders;

    @MockBean
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.set("Authorization", "Bearer test-token");

        // Mock Users-Service token validation to always succeed
        TokenValidationResponse ok = new TokenValidationResponse();
        ok.setValid(true);
        ok.setUserId("test-user");
        ok.setOrgCode("TEST_ORG");
        Mockito.when(userServiceClient.validateToken(ArgumentMatchers.any(ValidateTokenRequest.class)))
               .thenReturn(ok);
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void fullMailApisFlow() throws Exception {
        // ---------- 1. 创建邮件模板 ----------
        String uniq = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> createTpl = new HashMap<>();
        createTpl.put("templateName", "intTest_tpl_" + uniq);
        createTpl.put("contentType", 0);
        createTpl.put("contentTemplate", "Hello, {{name}}");
        createTpl.put("inputContent", "{" + "\"name\":\"Bob\"" + "}");
        createTpl.put("aiStrategyVersion", "v1");

        HttpEntity<String> createReq = new HttpEntity<>(objectMapper.writeValueAsString(createTpl), authHeaders);
        ResponseEntity<String> createResp = restTemplate.postForEntity(url("/api/v1/marketing/template/createTemplate"), createReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, createResp.getStatusCode());
        JsonNode createRoot = objectMapper.readTree(createResp.getBody());
        String templateId = createRoot.path("data").path("templateId").asText();
        Assertions.assertFalse(templateId.isEmpty(), "createTemplate 未返回 templateId");

        // ---------- 2. 修改邮件模板 ----------
        Map<String, Object> modifyTpl = new HashMap<>();
        modifyTpl.put("templateId", templateId);
        modifyTpl.put("templateName", "intTest_tpl_mod_" + uniq);
        modifyTpl.put("contentType", 1);
        modifyTpl.put("contentTemplate", "<h1>Hello {{name}}</h1>");
        modifyTpl.put("inputContent", "{" + "\"name\":\"Alice\"" + "}");
        modifyTpl.put("enableStatus", 1);
        modifyTpl.put("aiStrategyVersion", "v2");

        HttpEntity<String> modReq = new HttpEntity<>(objectMapper.writeValueAsString(modifyTpl), authHeaders);
        ResponseEntity<String> modResp = restTemplate.postForEntity(url("/api/v1/marketing/template/modifyTemplate"), modReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, modResp.getStatusCode());

        // ---------- 3. (改为) 再次分页查询模板列表，确认修改生效 ----------
        Map<String, Object> detailQuery = new HashMap<>();
        detailQuery.put("pageNum", 1);
        detailQuery.put("pageSize", 10);
        detailQuery.put("templateId", templateId);

        HttpEntity<String> detReq = new HttpEntity<>(objectMapper.writeValueAsString(detailQuery), authHeaders);
        ResponseEntity<String> detResp = restTemplate.postForEntity(url("/api/v1/marketing/template/pageTemplates"), detReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, detResp.getStatusCode());
        JsonNode detRoot = objectMapper.readTree(detResp.getBody());
        Assertions.assertTrue(detRoot.path("data").path("content").size() >= 1);
        boolean nameMatched = false;
        for (JsonNode item : detRoot.path("data").path("content")) {
            if (modifyTpl.get("templateName").equals(item.path("templateName").asText())) {
                nameMatched = true;
                break;
            }
        }
        Assertions.assertTrue(nameMatched, "Modified template name not found in page result");

        // ---------- 4. 分页查询模板列表 ----------
        Map<String, Object> pageTpl = new HashMap<>();
        pageTpl.put("pageNum", 1);
        pageTpl.put("pageSize", 10);
        HttpEntity<String> pageReq = new HttpEntity<>(objectMapper.writeValueAsString(pageTpl), authHeaders);
        ResponseEntity<String> pageResp = restTemplate.postForEntity(url("/api/v1/marketing/template/pageTemplates"), pageReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, pageResp.getStatusCode());
        JsonNode pageRoot = objectMapper.readTree(pageResp.getBody());
        Assertions.assertTrue(pageRoot.path("data").path("content").isArray());
        Assertions.assertTrue(pageRoot.path("data").path("content").size() >= 1);

        // ---------- 5. 创建邮件发送规则 ----------
        Map<String, Object> createRule = new HashMap<>();
        createRule.put("ruleName", "intTest_rule_" + uniq);
        createRule.put("templateId", templateId);
        createRule.put("ruleType", 1);
        createRule.put("recipients", List.of("bob@example.com"));
        HttpEntity<String> ruleCreateReq = new HttpEntity<>(objectMapper.writeValueAsString(createRule), authHeaders);
        ResponseEntity<String> ruleCreateResp = restTemplate.postForEntity(url("/api/v1/email/rules/createRule"), ruleCreateReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, ruleCreateResp.getStatusCode());
        String ruleId = objectMapper.readTree(ruleCreateResp.getBody()).path("data").path("ruleId").asText();
        Assertions.assertFalse(ruleId.isEmpty(), "createRule 未返回 ruleId");

        // ---------- 6. 修改发送规则 ----------
        Map<String, Object> modifyRule = new HashMap<>();
        modifyRule.put("ruleId", ruleId);
        modifyRule.put("ruleName", "intTest_rule_mod_" + uniq);
        modifyRule.put("templateId", templateId);
        modifyRule.put("ruleType", 1);
        modifyRule.put("recipients", List.of("alice@example.com"));
        modifyRule.put("enableStatus", 1);

        HttpEntity<String> ruleModReq = new HttpEntity<>(objectMapper.writeValueAsString(modifyRule), authHeaders);
        ResponseEntity<String> ruleModResp = restTemplate.postForEntity(url("/api/v1/email/rules/modifyRule"), ruleModReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, ruleModResp.getStatusCode());

        // ---------- 7. 获取规则详情 ----------
        Map<String, String> ruleDetailPayload = Map.of("ruleId", ruleId);
        HttpEntity<String> ruleDetReq = new HttpEntity<>(objectMapper.writeValueAsString(ruleDetailPayload), authHeaders);
        ResponseEntity<String> ruleDetResp = restTemplate.postForEntity(url("/api/v1/email/rules/getRuleDetail"), ruleDetReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, ruleDetResp.getStatusCode());
        JsonNode ruleDetRoot = objectMapper.readTree(ruleDetResp.getBody());
        Assertions.assertEquals(modifyRule.get("ruleName"), ruleDetRoot.path("data").path("ruleName").asText());

        // ---------- 8. 分页查询发送规则 ----------
        Map<String, Object> rulePage = new HashMap<>();
        rulePage.put("pageNum", 1);
        rulePage.put("pageSize", 10);
        HttpEntity<String> rulePageReq = new HttpEntity<>(objectMapper.writeValueAsString(rulePage), authHeaders);
        ResponseEntity<String> rulePageResp = restTemplate.postForEntity(url("/api/v1/email/rules/pageRules"), rulePageReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, rulePageResp.getStatusCode());
        JsonNode rulePageRoot = objectMapper.readTree(rulePageResp.getBody());
        Assertions.assertTrue(rulePageRoot.path("data").path("content").size() >= 1);

        // ---------- 9. 删除发送规则 ----------
        Map<String, String> delPayload = Map.of("ruleId", ruleId);
        HttpEntity<String> delRuleReq = new HttpEntity<>(objectMapper.writeValueAsString(delPayload), authHeaders);
        ResponseEntity<String> delRuleResp = restTemplate.postForEntity(url("/api/v1/email/rules/deleteRule"), delRuleReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, delRuleResp.getStatusCode());

        // ---------- 10. 删除邮件模板 ----------
        HttpEntity<String> delTplReq = new HttpEntity<>(objectMapper.writeValueAsString(detailQuery), authHeaders);
        ResponseEntity<String> delTplResp = restTemplate.postForEntity(url("/api/v1/marketing/template/deleteTemplate"), delTplReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, delTplResp.getStatusCode());
    }
}