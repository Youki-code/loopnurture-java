package org.springframework.samples.loopnurture.users.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.domain.repository.MarketingUserRepository;
import org.springframework.samples.loopnurture.users.domain.exception.LoginFailedException;
import org.springframework.samples.loopnurture.users.domain.exception.UserUniqExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.samples.loopnurture.users.server.config.JwtUtils;
import org.springframework.samples.loopnurture.users.infra.utils.RsaUtils;
import org.springframework.samples.loopnurture.users.infra.utils.SnowflakeIdGenerator;
import org.springframework.samples.loopnurture.users.server.controller.dto.*;
import org.springframework.samples.loopnurture.users.domain.model.vo.MarketingUserExtendsVO;
import org.springframework.samples.loopnurture.users.config.TestConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class MarketingUserServiceTest {

    @Autowired
    private MarketingUserService marketingUserService;

    @MockBean
    private MarketingUserRepository marketingUserRepository;

    @MockBean
    private SnowflakeIdGenerator idGenerator;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RsaUtils rsaUtils;

    private MarketingUserDO testUser;
    private RegisterRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = new MarketingUserDO();
        testUser.setSystemUserId(1L);
        testUser.setUserUniq("testuser");
        testUser.setPassword("hashedPassword");
        testUser.setAuthType(AuthTypeEnum.PASSWORD);
        testUser.setPrimaryEmail("test@example.com");
        testUser.setEmailVerified(false);
        testUser.setPhoneVerified(false);
        
        testRequest = new RegisterRequest();
        testRequest.setUserUniq("testuser");
        testRequest.setPassword("password");
        testRequest.setAuthType(AuthTypeEnum.PASSWORD.getCode());
    }

    @Test
    void testPasswordLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserUniq("testuser");
        loginRequest.setPassword("password");
        loginRequest.setAuthType(AuthTypeEnum.PASSWORD.getCode());

        when(marketingUserRepository.findByUserUniq(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtils.generateToken(any(MarketingUserDO.class))).thenReturn("test.jwt.token");

        LoginResponse response = marketingUserService.login(loginRequest);

        assertNotNull(response);
        assertEquals("test.jwt.token", response.getToken());
        assertEquals(testUser.getUserUniq(), response.getUserInfo().getUserUniq());
    }

    @Test
    void testPasswordLogin_WrongPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserUniq("testuser");
        loginRequest.setPassword("wrongpassword");
        loginRequest.setAuthType(AuthTypeEnum.PASSWORD.getCode());

        when(marketingUserRepository.findByUserUniq(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(LoginFailedException.class, () -> 
            marketingUserService.login(loginRequest)
        );
    }

    @Test
    void testOAuthLogin_Success() {
        MarketingUserDO oauthUser = new MarketingUserDO();
        oauthUser.setSystemUserId(2L);
        oauthUser.setUserUniq("oauth_user");
        oauthUser.setAuthType(AuthTypeEnum.GOOGLE_OAUTH);
        oauthUser.setOauthUserId("google_123");
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAuthType(AuthTypeEnum.GOOGLE_OAUTH.getCode());
        loginRequest.setOauthUserId("google_123");
        loginRequest.setOauthAccessToken("oauth_token");

        when(marketingUserRepository.findByOAuthInfo(anyString(), anyString()))
            .thenReturn(Optional.of(oauthUser));
        when(jwtUtils.generateToken(any(MarketingUserDO.class))).thenReturn("test.jwt.token");

        LoginResponse response = marketingUserService.login(loginRequest);

        assertNotNull(response);
        assertEquals("test.jwt.token", response.getToken());
        assertEquals(oauthUser.getUserUniq(), response.getUserInfo().getUserUniq());
        assertEquals(AuthTypeEnum.GOOGLE_OAUTH.getCode(), response.getUserInfo().getAuthType());
    }

    @Test
    void register_Success() {
        when(marketingUserRepository.existsByUserUniq(anyString())).thenReturn(false);
        when(rsaUtils.decrypt(anyString())).thenReturn("decrypted_password");
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(idGenerator.nextId()).thenReturn(1L);
        when(marketingUserRepository.save(any(MarketingUserDO.class))).thenReturn(testUser);

        MarketingUserDO result = marketingUserService.register(testRequest);

        assertNotNull(result);
        assertEquals(testUser.getSystemUserId(), result.getSystemUserId());
        assertEquals(testUser.getUserUniq(), result.getUserUniq());
        verify(marketingUserRepository).save(any(MarketingUserDO.class));
    }

    @Test
    void register_UserExists() {
        when(marketingUserRepository.existsByUserUniq(anyString())).thenReturn(true);

        assertThrows(UserUniqExistsException.class, () -> 
            marketingUserService.register(testRequest)
        );
    }

    @Test
    void testExtendsInfo_OAuth() {
        MarketingUserDO user = new MarketingUserDO();
        user.setAuthType(AuthTypeEnum.GOOGLE_OAUTH);
        
        MarketingUserExtendsVO extendsInfo = new MarketingUserExtendsVO();
        extendsInfo.setOauthAccessToken("test_token");
        extendsInfo.setOauthRefreshToken("refresh_token");
        user.setExtendInfo(extendsInfo);

        when(marketingUserRepository.save(any(MarketingUserDO.class))).thenReturn(user);

        MarketingUserDO savedUser = marketingUserService.save(user);
        assertNotNull(savedUser.getExtendInfo());
        assertEquals("test_token", savedUser.getExtendInfo().getOauthAccessToken());
    }

    @Test
    void testEmailVerification() {
        testUser.setEmailVerified(false);
        when(marketingUserRepository.findByUserUniq(anyString())).thenReturn(Optional.of(testUser));
        when(marketingUserRepository.save(any(MarketingUserDO.class))).thenReturn(testUser);

        marketingUserService.verifyEmail(testUser.getUserUniq());
        assertTrue(testUser.getEmailVerified());
    }

    @Test
    void testPhoneVerification() {
        testUser.setPhoneVerified(false);
        when(marketingUserRepository.findByUserUniq(anyString())).thenReturn(Optional.of(testUser));
        when(marketingUserRepository.save(any(MarketingUserDO.class))).thenReturn(testUser);

        marketingUserService.verifyPhone(testUser.getUserUniq());
        assertTrue(testUser.getPhoneVerified());
    }

    @Test
    void findByUserUniq_Success() {
        when(marketingUserRepository.findByUserUniq(anyString())).thenReturn(Optional.of(testUser));

        MarketingUserDO result = marketingUserService.findByUserUniq("testuser");

        assertNotNull(result);
        assertEquals(testUser.getUserUniq(), result.getUserUniq());
    }

    @Test
    void findByUserUniq_NotFound() {
        when(marketingUserRepository.findByUserUniq(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
            marketingUserService.findByUserUniq("nonexistent")
        );
    }

    @Test
    void generateToken_Success() {
        String expectedToken = "test.jwt.token";
        when(marketingUserRepository.findByUserUniq(anyString())).thenReturn(Optional.of(testUser));
        when(jwtUtils.generateToken(any(MarketingUserDO.class))).thenReturn(expectedToken);

        String result = marketingUserService.generateToken(testUser.getUserUniq());

        assertEquals(expectedToken, result);
        verify(jwtUtils).generateToken(testUser);
    }
} 