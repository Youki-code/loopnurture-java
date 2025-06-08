package org.springframework.samples.loopnurture.users.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.service.MarketingUserService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.samples.loopnurture.users.server.controller.dto.LoginRequest;
import org.springframework.samples.loopnurture.users.server.controller.dto.LoginResponse;
import org.springframework.samples.loopnurture.users.server.controller.dto.TokenValidationRequest;
import org.springframework.samples.loopnurture.users.server.controller.dto.TokenValidationResponse;
import org.springframework.samples.loopnurture.users.infra.utils.RsaUtils;
import org.springframework.samples.loopnurture.users.config.TestConfig;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.verify;

@WebMvcTest(AuthController.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarketingUserService marketingUserService;

    @MockBean
    private RsaUtils rsaUtils;

    private ObjectMapper objectMapper = new ObjectMapper();

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private TokenValidationRequest tokenRequest;
    private TokenValidationResponse tokenResponse;

    @BeforeEach
    void setUp() {
        // 设置登录请求
        loginRequest = new LoginRequest();
        loginRequest.setUserUniq("testuser");
        loginRequest.setPassword("encrypted_password");
        loginRequest.setAuthType(AuthTypeEnum.PASSWORD.getCode());

        // 设置登录响应
        loginResponse = new LoginResponse();
        loginResponse.setToken("test.jwt.token");
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setSystemUserId(1L);
        userInfo.setUserUniq("testuser");
        userInfo.setAuthType(AuthTypeEnum.PASSWORD.getCode());
        loginResponse.setUserInfo(userInfo);

        // 设置令牌验证请求
        tokenRequest = new TokenValidationRequest();
        tokenRequest.setToken("test.jwt.token");

        // 设置令牌验证响应
        tokenResponse = new TokenValidationResponse();
        tokenResponse.setValid(true);
        tokenResponse.setUserId(1L);
        tokenResponse.setUserUniq("testuser");
    }

    @Test
    void getPublicKey_Success() throws Exception {
        String publicKey = "test-public-key";
        when(rsaUtils.getPublicKey()).thenReturn(publicKey);

        mockMvc.perform(get("/api/v1/auth/public-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(publicKey));

        verify(rsaUtils).getPublicKey();
    }

    @Test
    void login_WithValidPasswordCredentials_Success() throws Exception {
        when(marketingUserService.login(any(LoginRequest.class)))
            .thenReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.token").value("test.jwt.token"));

        verify(marketingUserService).login(any(LoginRequest.class));
    }

    @Test
    void login_WithOAuthCredentials_Success() throws Exception {
        // 设置OAuth登录请求
        loginRequest.setAuthType(AuthTypeEnum.GOOGLE_OAUTH.getCode());
        loginRequest.setOauthUserId("google_123");
        loginRequest.setOauthAccessToken("oauth_token");
        loginRequest.setPassword(null);

        // 设置OAuth登录响应
        loginResponse.getUserInfo().setAuthType(AuthTypeEnum.GOOGLE_OAUTH.getCode());
        loginResponse.getUserInfo().setUserUniq("google_123");

        when(marketingUserService.login(any(LoginRequest.class)))
            .thenReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.token").value("test.jwt.token"));

        verify(marketingUserService).login(any(LoginRequest.class));
    }

    @Test
    void validateToken_Success() throws Exception {
        TokenValidationResponse validResponse = new TokenValidationResponse();
        validResponse.setValid(true);
        validResponse.setUserId(1L);
        validResponse.setUserUniq("testuser");

        when(marketingUserService.validateToken(anyString()))
            .thenReturn(validResponse);

        mockMvc.perform(post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.valid").value(true))
            .andExpect(jsonPath("$.data.userId").value(1))
            .andExpect(jsonPath("$.data.userUniq").value("testuser"));

        verify(marketingUserService).validateToken(tokenRequest.getToken());
    }

    @Test
    void validateToken_Invalid() throws Exception {
        TokenValidationResponse invalidResponse = new TokenValidationResponse();
        invalidResponse.setValid(false);
        invalidResponse.setErrorMessage("Invalid token");

        when(marketingUserService.validateToken(anyString()))
            .thenReturn(invalidResponse);

        mockMvc.perform(post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.valid").value(false))
            .andExpect(jsonPath("$.data.errorMessage").value("Invalid token"));

        verify(marketingUserService).validateToken(tokenRequest.getToken());
    }
} 