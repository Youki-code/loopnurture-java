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
import org.springframework.samples.loopnurture.users.domain.enums.UserTypeEnum;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.service.MarketingUserService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.samples.loopnurture.users.server.controller.dto.*;
import org.springframework.samples.loopnurture.users.config.TestConfig;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MarketingUserService userService;

    private MarketingUserDO testUser;
    private UserRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        testUser = new MarketingUserDO();
        testUser.setSystemUserId(1L);
        testUser.setUserUniq("testuser");
        testUser.setAuthType(AuthTypeEnum.PASSWORD);
        testUser.setPrimaryEmail("test@example.com");
        testUser.setNickname("Test User");
        testUser.setUserType(UserTypeEnum.PERSONAL);

        registrationRequest = new UserRegistrationRequest();
        registrationRequest.setUserUniq("testuser");
        registrationRequest.setEncryptedPassword("encrypted_password");
        registrationRequest.setAuthType(AuthTypeEnum.PASSWORD);
        registrationRequest.setUserType(UserTypeEnum.PERSONAL);
        registrationRequest.setPrimaryEmail("test@example.com");
        registrationRequest.setNickname("Test User");
    }

    @Test
    void register_Success() throws Exception {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("test.jwt.token");
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setSystemUserId(1L);
        userInfo.setUserUniq("testuser");
        userInfo.setAuthType(AuthTypeEnum.PASSWORD.getCode());
        loginResponse.setUserInfo(userInfo);

        when(userService.register(any(RegisterRequest.class)))
            .thenReturn(testUser);

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userUniq").value("testuser"))
                .andExpect(jsonPath("$.data.systemUserId").value(1));
    }

    @Test
    void getUser_Success() throws Exception {
        when(userService.findByUserUniq("testuser")).thenReturn(testUser);

        mockMvc.perform(get("/api/v1/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.systemUserId").value(1))
                .andExpect(jsonPath("$.data.userUniq").value("testuser"))
                .andExpect(jsonPath("$.data.primaryEmail").value("test@example.com"))
                .andExpect(jsonPath("$.data.nickname").value("Test User"));
    }

    @Test
    void getUser_NotFound() throws Exception {
        when(userService.findByUserUniq("nonexistent")).thenReturn(null);

        mockMvc.perform(get("/api/v1/users/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_Success() throws Exception {
        UserResponse updateRequest = new UserResponse();
        updateRequest.setNickname("Updated Name");
        updateRequest.setPrimaryEmail("updated@example.com");

        testUser.setNickname("Updated Name");
        testUser.setPrimaryEmail("updated@example.com");

        when(userService.findByUserUniq("testuser")).thenReturn(testUser);
        when(userService.save(any(MarketingUserDO.class))).thenReturn(testUser);

        mockMvc.perform(put("/api/v1/users/testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value("Updated Name"))
                .andExpect(jsonPath("$.data.primaryEmail").value("updated@example.com"));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        UserResponse updateRequest = new UserResponse();
        updateRequest.setNickname("Updated Name");

        when(userService.findByUserUniq("nonexistent")).thenReturn(null);

        mockMvc.perform(put("/api/v1/users/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
} 