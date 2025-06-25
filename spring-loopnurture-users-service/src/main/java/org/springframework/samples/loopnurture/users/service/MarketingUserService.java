package org.springframework.samples.loopnurture.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.AccountStatusEnum;
import org.springframework.samples.loopnurture.users.domain.enums.UserTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.LanguagePreferenceEnum;
import org.springframework.samples.loopnurture.users.domain.exception.LoginFailedException;
import org.springframework.samples.loopnurture.users.domain.exception.UserUniqExistsException;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.domain.model.vo.MarketingUserExtendsVO;
import org.springframework.samples.loopnurture.users.domain.repository.MarketingUserRepository;
import org.springframework.samples.loopnurture.users.domain.repository.UserOrganizationRepository;
import org.springframework.samples.loopnurture.users.server.config.JwtUtils;
import org.springframework.samples.loopnurture.users.infra.utils.RsaUtils;
import org.springframework.samples.loopnurture.users.infra.utils.SnowflakeIdGenerator;
import org.springframework.samples.loopnurture.users.server.controller.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.users.service.OrganizationService;
import org.springframework.samples.loopnurture.users.service.UserOrganizationService;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.domain.enums.UserRoleEnum;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.model.UserOrganizationDO;
import org.springframework.samples.loopnurture.users.service.oauth.GoogleTokenValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 营销用户服务
 */
@Service
@RequiredArgsConstructor
public class MarketingUserService {

    private final MarketingUserRepository userRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RsaUtils rsaUtils;
    private final OrganizationService organizationService;
    private final UserOrganizationService userOrganizationService;
    private final UserOrganizationRepository userOrganizationRepository;
    private final GoogleTokenValidator googleTokenValidator;
    @org.springframework.beans.factory.annotation.Value("${google.oauth.verify-token:true}")
    private boolean googleVerifyToken;

    /**
     * 用户注册
     */
    @Transactional
    public MarketingUserDO register(RegisterRequest request) {
        request.validate();

        // 检查用户名是否已存在
        if (StringUtils.hasText(request.getUserUniq()) && 
            userRepository.existsByUserUniq(request.getUserUniq())) {
            throw new UserUniqExistsException("User already exists");
        }

        // 创建新用户
        MarketingUserDO newUser = new MarketingUserDO();
        
        // 设置系统用户ID
        newUser.setSystemUserId(idGenerator.nextId());
        
        // 设置用户唯一标识
        String effectiveUserUniq = StringUtils.hasText(request.getUserUniq()) ? 
            request.getUserUniq() : String.valueOf(newUser.getSystemUserId());
        newUser.setUserUniq(effectiveUserUniq);

        // 设置认证类型和相关信息
        AuthTypeEnum authType = AuthTypeEnum.fromCode(request.getAuthType());
        newUser.setAuthType(authType);
        
        if (authType == AuthTypeEnum.PASSWORD) {
            String decryptedPassword = rsaUtils.decrypt(request.getPassword());
            newUser.setPassword(passwordEncoder.encode(decryptedPassword));
        } else if (authType == AuthTypeEnum.GOOGLE_OAUTH) {
            MarketingUserExtendsVO extendsInfo = new MarketingUserExtendsVO();
            extendsInfo.setOauthAccessToken(request.getOauthAccessToken());

            if (googleVerifyToken) {
                // 开启验证：本地校验 Google ID Token
                var payload = googleTokenValidator.validate(request.getOauthAccessToken());
                newUser.setOauthUserId(payload.getGoogleId());

                // 补全基本信息
                if (!org.apache.commons.lang3.StringUtils.isNotBlank(request.getPrimaryEmail())) {
                    newUser.setPrimaryEmail(payload.getEmail());
                }
                if (!org.apache.commons.lang3.StringUtils.isNotBlank(request.getNickname())) {
                    newUser.setNickname(payload.getName());
                }
                if (!org.apache.commons.lang3.StringUtils.isNotBlank(request.getAvatarUrl())) {
                    newUser.setAvatarUrl(payload.getAvatar());
                }

                extendsInfo.setOauthIdToken(request.getOauthAccessToken());
            } else {
                // 关闭验证：直接信任请求提供的 oauthUserId
                newUser.setOauthUserId(request.getOauthUserId());
            }

            newUser.setExtendInfo(extendsInfo);
        } else {
            newUser.setOauthUserId(request.getOauthUserId());
            MarketingUserExtendsVO extendsInfo = new MarketingUserExtendsVO();
            extendsInfo.setOauthAccessToken(request.getOauthAccessToken());
            newUser.setExtendInfo(extendsInfo);
        }

        // 设置用户类型
        if (request.getUserType() != null) {
            newUser.setUserType(UserTypeEnum.fromCode(request.getUserType()));
        }

        // 设置基本信息
        newUser.setNickname(request.getNickname());
        newUser.setPrimaryEmail(request.getPrimaryEmail());
        newUser.setPhone(request.getPhone());
        newUser.setAvatarUrl(request.getAvatarUrl());
        newUser.setTimezone(request.getTimezone());
        
        // 设置语言偏好
        if (StringUtils.hasText(request.getLanguagePreference())) {
            newUser.setLanguagePreference(LanguagePreferenceEnum.valueOf(request.getLanguagePreference()));
        } else {
            newUser.setLanguagePreference(LanguagePreferenceEnum.EN_US);
        }

        // 设置初始状态
        newUser.setEmailVerified(false);
        newUser.setPhoneVerified(false);
        newUser.setAccountStatus(AccountStatusEnum.ACTIVE);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        // -------- 创建默认组织 --------
        String defaultOrgCode = "org_" + newUser.getSystemUserId();
        OrganizationDO defaultOrg = new OrganizationDO();
        defaultOrg.setOrgCode(defaultOrgCode);
        defaultOrg.setOrgName(newUser.getNickname() != null ? newUser.getNickname() + "'s Org" : newUser.getUserUniq() + "'s Org");
        defaultOrg.setOrgType(OrganizationTypeEnum.PERSONAL);
        defaultOrg.setStatus(OrganizationStatusEnum.ACTIVE);
        defaultOrg.setCreatedAt(LocalDateTime.now());
        defaultOrg.setUpdatedAt(LocalDateTime.now());
        defaultOrg.setCreatedBy(newUser.getSystemUserId());
        defaultOrg.setUpdatedBy(newUser.getSystemUserId());
        organizationService.createOrganization(defaultOrg);

        // 将用户加入默认组织并设置当前组织
        newUser.setCurrentOrgCode(defaultOrgCode);

        // 先保存用户，再建立关联，确保用户ID存在
        MarketingUserDO savedUser = userRepository.save(newUser);
        userOrganizationService.addUserToOrganization(savedUser.getSystemUserId(), defaultOrgCode, UserRoleEnum.ADMIN, String.valueOf(savedUser.getSystemUserId()));

        return savedUser;
    }

    /**
     * 用户登录
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        request.validate();
        
        AuthTypeEnum authType = AuthTypeEnum.fromCode(request.getAuthType());
        MarketingUserDO user;

        if (authType == AuthTypeEnum.PASSWORD) {
            if (request.isEmailLogin()) {
                user = userRepository.findByPrimaryEmail(request.getEmail())
                    .orElseThrow(() -> new LoginFailedException("Invalid email or password"));
            } else if (request.isPhoneLogin()) {
                user = userRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new LoginFailedException("Invalid phone or password"));
            } else {
                user = userRepository.findByUserUniq(request.getUserUniq())
                    .orElseThrow(() -> new LoginFailedException("Invalid username or password"));
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new LoginFailedException("Invalid password");
            }
        } else {
            user = userRepository.findByOAuthInfo(request.getOauthUserId(), authType.getCode())
                .orElseThrow(() -> new LoginFailedException("OAuth user not found"));
            
            // Update OAuth token if provided
            if (StringUtils.hasText(request.getOauthAccessToken())) {
                updateOAuthInfo(user, request.getOauthAccessToken());
                // Persist latest OAuth token. Some mocks may return null, so guard against it
                MarketingUserDO persisted = userRepository.save(user);
                if (persisted != null) {
                    user = persisted;
                }
            }
        }

        return createLoginResponse(user);
    }

    /**
     * 验证 token
     */
    @Transactional(readOnly = true)
    public TokenValidationResponse validateToken(String token) {
        TokenValidationResponse response = new TokenValidationResponse();
        
        try {
            if (!jwtUtils.validateToken(token)) {
                response.setValid(false);
                response.setErrorMessage("Invalid token");
                return response;
            }

            String userUniq = jwtUtils.getUserUniqFromToken(token);
            MarketingUserDO user = userRepository.findByUserUniq(userUniq)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
            String orgCode = findFirstOrgCode(user.getSystemUserId());

            response.setValid(true);
            response.setUserId(user.getSystemUserId());
            response.setUserUniq(user.getUserUniq());
            response.setOrgCode(orgCode);
        } catch (Exception e) {
            response.setValid(false);
            response.setErrorMessage(e.getMessage());
        }

        System.out.println("validateToken.response: " + response.toString());
        return response;
    }

    /**
     * 根据 userUniq 查找用户
     */
    @Transactional(readOnly = true)
    public MarketingUserDO findByUserUniq(String userUniq) {
        return userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * 生成 token
     */
    @Transactional
    public String generateToken(String userUniq) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return jwtUtils.generateToken(user);
    }

    /**
     * 更新用户扩展信息
     */
    @Transactional
    public MarketingUserDO updateExtendsInfo(String userUniq, MarketingUserExtendsVO extendsInfo) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setExtendInfo(extendsInfo);
        return userRepository.save(user);
    }

    /**
     * 验证邮箱
     */
    @Transactional
    public void verifyEmail(String userUniq) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    /**
     * 验证手机号
     */
    @Transactional
    public void verifyPhone(String userUniq) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPhoneVerified(true);
        userRepository.save(user);
    }

    /**
     * 创建新用户
     */
    private MarketingUserDO createNewUser(String userUniq, String password, String oauthUserId, 
            String oauthAccessToken, AuthTypeEnum authType) {
        
        // 生成系统用户ID
        long systemUserId = idGenerator.nextId();
        
        // 如果未提供userUniq，使用systemUserId
        String effectiveUserUniq = StringUtils.hasText(userUniq) ? 
            userUniq : String.valueOf(systemUserId);

        MarketingUserDO user = new MarketingUserDO();
        user.setSystemUserId(systemUserId);
        user.setUserUniq(effectiveUserUniq);
        user.setAuthType(authType);
        
        if (authType == AuthTypeEnum.PASSWORD) {
            user.setPassword(passwordEncoder.encode(password));
        } else {
            user.setOauthUserId(oauthUserId);
            updateOAuthInfo(user, oauthAccessToken);
        }

        return userRepository.save(user);
    }

    /**
     * 更新用户的OAuth信息
     */
    private void updateOAuthInfo(MarketingUserDO user, String oauthAccessToken) {
        if (user.getExtendInfo() == null) {
            user.setExtendInfo(new MarketingUserExtendsVO());
        }
        user.getExtendInfo().setOauthAccessToken(oauthAccessToken);
    }

    /**
     * 创建登录响应
     */
    private LoginResponse createLoginResponse(MarketingUserDO user) {
        LoginResponse response = new LoginResponse();
        response.setToken(jwtUtils.generateToken(user));
        
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setSystemUserId(user.getSystemUserId());
        userInfo.setUserUniq(user.getUserUniq());
        userInfo.setNickname(user.getNickname());
        userInfo.setPrimaryEmail(user.getPrimaryEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatarUrl(user.getAvatarUrl());
        userInfo.setLanguagePreference(user.getLanguagePreference() != null ? user.getLanguagePreference().getCode() : null);
        userInfo.setTimezone(user.getTimezone());
        userInfo.setAuthType(user.getAuthType() != null ? user.getAuthType().getCode() : null);
        userInfo.setOrgCode(findFirstOrgCode(user.getSystemUserId()));
        userInfo.setEmailVerified(user.getEmailVerified());
        userInfo.setPhoneVerified(user.getPhoneVerified());
        response.setUserInfo(userInfo);
        
        return response;
    }

    /**
     * 检查用户名是否可用
     */
    @Transactional(readOnly = true)
    public boolean isUserUniqAvailable(String userUniq) {
        return !userRepository.existsByUserUniq(userUniq);
    }

    @Transactional(readOnly = true)
    public List<MarketingUserDO> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MarketingUserDO> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<MarketingUserDO> findByPrimaryEmail(String primaryEmail) {
        return userRepository.findByPrimaryEmail(primaryEmail);
    }

    @Transactional(readOnly = true)
    public Optional<MarketingUserDO> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Transactional(readOnly = true)
    public List<MarketingUserDO> findByOrgCode(String orgCode) {
        return userRepository.findByOrgCode(orgCode);
    }

    public org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO save(org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserUniq(String userUniq) {
        return userRepository.existsByUserUniq(userUniq);
    }

    @Transactional(readOnly = true)
    public boolean existsByPrimaryEmail(String primaryEmail) {
        return userRepository.existsByPrimaryEmail(primaryEmail);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * 重置密码
     */
    @Transactional
    public void resetPassword(String userUniq, String encryptedNewPassword) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (user.getAuthType() != AuthTypeEnum.PASSWORD) {
            throw new IllegalStateException("Cannot reset password for OAuth user");
        }

        String decryptedPassword = rsaUtils.decrypt(encryptedNewPassword);
        user.setPassword(passwordEncoder.encode(decryptedPassword));
        userRepository.save(user);
    }

    /**
     * 注销账户
     * 注销后的账户将被标记为已删除，但不会物理删除
     */
    @Transactional
    public void deactivateAccount(String userUniq) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setAccountStatus(AccountStatusEnum.DELETED);
        userRepository.save(user);
    }

    /**
     * 发送邮箱验证码
     */
    public void sendEmailVerificationCode(String userUniq) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (user.getEmailVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        // TODO: 实现邮箱验证码发送逻辑
    }

    /**
     * 发送手机验证码
     */
    public void sendPhoneVerificationCode(String userUniq) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (user.getPhoneVerified()) {
            throw new IllegalStateException("Phone already verified");
        }

        // TODO: 实现手机验证码发送逻辑
    }

    /**
     * 验证邮箱验证码
     */
    @Transactional
    public void verifyEmailCode(String userUniq, String code) {
        // TODO: 实现邮箱验证码验证逻辑
        verifyEmail(userUniq);
    }

    /**
     * 验证手机验证码
     */
    @Transactional
    public void verifyPhoneCode(String userUniq, String code) {
        // TODO: 实现手机验证码验证逻辑
        verifyPhone(userUniq);
    }

    /**
     * 根据系统用户ID查询
     */
    @Transactional(readOnly = true)
    public MarketingUserDO findBySystemUserId(Long systemUserId) {
        return userRepository.findBySystemUserId(systemUserId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * 根据系统用户ID获取第一个未删除的orgCode
     * @param systemUserId
     * @return
     */
    public String findFirstOrgCode(Long systemUserId) {
        List<UserOrganizationDO> userOrganizations = userOrganizationRepository.findBySystemUserId(systemUserId);
        if (userOrganizations.isEmpty()) {
            return null;
        }
        return userOrganizations.get(0).getOrgCode();
    }
} 