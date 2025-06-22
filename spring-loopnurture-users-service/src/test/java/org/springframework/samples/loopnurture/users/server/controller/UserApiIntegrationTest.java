package org.springframework.samples.loopnurture.users.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.loopnurture.users.domain.repository.MarketingUserRepository;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.domain.repository.UserOrganizationRepository;
import org.junit.jupiter.api.AfterEach;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Full-stack integration test against real PostgreSQL (profile = remote).
 *
 * Flow:
 * 1. register user  → get token & userInfo
 * 2. GET /users/{id} with Authorization header and compare fields
 * 3. POST /auth/validate    → assert token valid
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("remote")
class UserApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MarketingUserRepository marketingUserRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserOrganizationRepository userOrganizationRepository;

    private Long lastSystemUserId;
    private String lastOrgCode;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void register_getUser_validateToken() throws Exception {
        // -------- 1. 注册用户 --------
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        Map<String, Object> registerPayload = new HashMap<>();
        registerPayload.put("authType", 2); // GOOGLE_OAUTH
        registerPayload.put("oauthUserId", "intTest_google_" + randomSuffix);
        registerPayload.put("oauthAccessToken", "dummy_google_token");
        registerPayload.put("userUniq", "intTest_" + randomSuffix);
        registerPayload.put("primaryEmail", "intTest_" + randomSuffix + "@example.com");
        registerPayload.put("userType", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> req = new HttpEntity<>(objectMapper.writeValueAsString(registerPayload), headers);

        ResponseEntity<String> resp = restTemplate.postForEntity(url("/api/v1/users/register"), req, String.class);
        Assertions.assertEquals(HttpStatus.OK, resp.getStatusCode(), "注册接口未返回 200");

        JsonNode root = objectMapper.readTree(resp.getBody());
        String token = root.path("data").path("token").asText();
        JsonNode userInfo = root.path("data").path("userInfo");
        long userId = userInfo.path("systemUserId").asLong();
        Assertions.assertTrue(token.isEmpty() == false, "注册接口未返回 token");
        Assertions.assertTrue(userId > 0, "注册接口未返回有效的 userId");

        lastSystemUserId = userId;
        lastOrgCode = "org_" + userId;

        // -------- 2. 查询用户信息 --------
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(token);
        HttpEntity<Void> authReq = new HttpEntity<>(authHeaders);

        ResponseEntity<String> userResp = restTemplate.exchange(url("/api/v1/users/" + userId), HttpMethod.GET, authReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, userResp.getStatusCode());
        JsonNode userRoot = objectMapper.readTree(userResp.getBody());
        String fetchedUniq = userRoot.path("data").path("userUniq").asText();
        Assertions.assertEquals(registerPayload.get("userUniq"), fetchedUniq, "查询到的 userUniq 不匹配");

        // -------- 3. 验证 token --------
        Map<String, String> validatePayload = Map.of("token", token);
        HttpEntity<String> validateReq = new HttpEntity<>(objectMapper.writeValueAsString(validatePayload), headers);
        ResponseEntity<String> validateResp = restTemplate.postForEntity(url("/api/v1/auth/validate"), validateReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, validateResp.getStatusCode());
        JsonNode validRoot = objectMapper.readTree(validateResp.getBody());
        boolean valid = validRoot.path("data").path("valid").asBoolean();
        long validatedUserId = validRoot.path("data").path("userId").asLong();
        Assertions.assertTrue(valid, "validateToken 返回 invalid");
        Assertions.assertEquals(userId, validatedUserId, "validateToken 返回的 userId 不匹配");
    }

    /**
     * 公开接口：获取 RSA 公钥
     */
    @Test
    void getPublicKey_success() throws Exception {
        ResponseEntity<String> resp = restTemplate.getForEntity(url("/api/v1/auth/public-key"), String.class);
        Assertions.assertEquals(HttpStatus.OK, resp.getStatusCode());

        JsonNode root = objectMapper.readTree(resp.getBody());
        String key = root.path("data").asText();
        Assertions.assertTrue(key.matches("^[A-Za-z0-9+/=]+$"), "返回的公钥应为Base64字符串");
        Assertions.assertTrue(key.length() > 300, "公钥长度过短");
    }

    /**
     * 完整 OAuth 注册 → 登录 → 验证 token 流程
     */
    @Test
    void oauthRegister_login_validate_flow() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        // 1. register
        Map<String, Object> reg = new HashMap<>();
        reg.put("authType", 2);
        reg.put("oauthUserId", "it_oauth_" + suffix);
        reg.put("oauthAccessToken", "dummy_token");
        reg.put("userUniq", "it_oauth_user_" + suffix);
        reg.put("primaryEmail", suffix + "@example.com");
        reg.put("userType", 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> regReq = new HttpEntity<>(objectMapper.writeValueAsString(reg), headers);
        ResponseEntity<String> regResp = restTemplate.postForEntity(url("/api/v1/users/register"), regReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, regResp.getStatusCode());

        JsonNode regRoot = objectMapper.readTree(regResp.getBody());
        String regToken = regRoot.path("data").path("token").asText();
        long sysUserId = regRoot.path("data").path("userInfo").path("systemUserId").asLong();
        Assertions.assertFalse(regToken.isEmpty(), "注册未返回 token");

        lastSystemUserId = sysUserId;
        lastOrgCode = "org_" + sysUserId;

        // 2. login (OAuth)
        Map<String, Object> login = new HashMap<>();
        login.putAll(reg); // same credentials

        HttpEntity<String> loginReq = new HttpEntity<>(objectMapper.writeValueAsString(login), headers);
        ResponseEntity<String> loginResp = restTemplate.postForEntity(url("/api/v1/auth/login"), loginReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        String loginToken = objectMapper.readTree(loginResp.getBody()).path("data").path("token").asText();
        Assertions.assertFalse(loginToken.isEmpty(), "登录未返回 token");

        // 3. validate login token
        Map<String, String> vPayload = Map.of("token", loginToken);
        HttpEntity<String> vReq = new HttpEntity<>(objectMapper.writeValueAsString(vPayload), headers);
        ResponseEntity<String> vResp = restTemplate.postForEntity(url("/api/v1/auth/validate"), vReq, String.class);
        Assertions.assertEquals(HttpStatus.OK, vResp.getStatusCode());
        JsonNode vRoot = objectMapper.readTree(vResp.getBody());
        Assertions.assertTrue(vRoot.path("data").path("valid").asBoolean());
        Assertions.assertEquals(sysUserId, vRoot.path("data").path("userId").asLong());
    }

    @AfterEach
    void cleanupInsertedData() {
        if (lastSystemUserId != null) {
            // 软删除用户-组织关联
            userOrganizationRepository.deleteBySystemUserId(lastSystemUserId);
            // 软删除用户本身
            marketingUserRepository.softDeleteBySystemUserId(lastSystemUserId);
        }
        if (lastOrgCode != null) {
            // 软删除组织
            organizationRepository.deleteByOrgCode(lastOrgCode);
        }
        lastSystemUserId = null;
        lastOrgCode = null;
    }
} 