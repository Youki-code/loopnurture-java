package org.springframework.samples.loopnurture.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.exception.LoginFailedException;
import org.springframework.samples.loopnurture.users.domain.exception.UserUniqExistsException;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.domain.model.vo.MarketingUserExtendsVO;
import org.springframework.samples.loopnurture.users.domain.repository.MarketingUserRepository;
import org.springframework.samples.loopnurture.users.infra.utils.JwtUtils;
import org.springframework.samples.loopnurture.users.infra.utils.RsaUtils;
import org.springframework.samples.loopnurture.users.infra.utils.SnowflakeIdGenerator;
import org.springframework.samples.loopnurture.users.server.dto.LoginRequest;
import org.springframework.samples.loopnurture.users.server.dto.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    /**
     * 用户登录
     * 支持密码登录和OAuth登录
     *
     * @param request 登录请求
     * @return 登录响应（包含JWT令牌和用户信息）
     * @throws LoginFailedException 登录失败时抛出
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        request.validate();

        if (request.getAuthType() == AuthTypeEnum.PASSWORD) {
            return passwordLogin(request.getUserUniq(), request.getPassword());
        } else {
            return oauthLogin(request.getOauthUserId(), request.getOauthAccessToken(), request.getAuthType());
        }
    }

    /**
     * 密码登录
     */
    private LoginResponse passwordLogin(String userUniq, String encryptedPassword) {
        MarketingUserDO user = userRepository.findByUserUniq(userUniq)
            .orElseThrow(() -> new LoginFailedException("用户名或密码错误"));

        if (user.getAuthType() != AuthTypeEnum.PASSWORD) {
            throw new LoginFailedException("该账号为OAuth账号，请使用对应的OAuth方式登录");
        }

        try {
            // 解密前端传来的密码
            String decryptedPassword = rsaUtils.decrypt(encryptedPassword);
            // 验证密码
            if (!passwordEncoder.matches(decryptedPassword, user.getPassword())) {
                throw new LoginFailedException("用户名或密码错误");
            }
        } catch (RuntimeException e) {
            throw new LoginFailedException("密码解密失败，请重试");
        }

        return createLoginResponse(user);
    }

    /**
     * OAuth登录
     */
    private LoginResponse oauthLogin(String oauthUserId, String oauthAccessToken, AuthTypeEnum authType) {
        MarketingUserDO user = userRepository.findByOAuthInfo(oauthUserId, authType.name())
            .orElseThrow(() -> new LoginFailedException("OAuth用户不存在，请先注册"));

        if (user.getAuthType() != authType) {
            throw new LoginFailedException("认证类型不匹配");
        }

        // 更新OAuth token
        updateOAuthInfo(user, oauthAccessToken);
        user = userRepository.save(user);

        return createLoginResponse(user);
    }

    /**
     * 处理用户注册
     * 如果提供了userUniq，会检查是否已存在
     * 如果未提供userUniq，则使用systemUserId作为userUniq
     *
     * @param userUniq 用户输入的登录名（可选）
     * @param encryptedPassword 加密后的密码（密码注册时必填）
     * @param oauthUserId OAuth用户ID（可选）
     * @param oauthAccessToken OAuth访问令牌（可选）
     * @param authType 认证类型
     * @return 用户信息和JWT令牌
     * @throws UserUniqExistsException 如果提供的userUniq已存在
     */
    @Transactional
    public LoginResponse registerUser(String userUniq, String encryptedPassword, String oauthUserId, 
            String oauthAccessToken, AuthTypeEnum authType) {
        
        // 验证参数
        if (authType == AuthTypeEnum.PASSWORD) {
            if (!StringUtils.hasText(encryptedPassword)) {
                throw new IllegalArgumentException("密码注册时，密码不能为空");
            }
        }
        
        // 如果提供了userUniq，先检查是否已存在
        if (StringUtils.hasText(userUniq) && userRepository.existsByUserUniq(userUniq)) {
            throw new UserUniqExistsException(userUniq);
        }

        // 如果是OAuth用户，检查是否已存在
        if (authType != AuthTypeEnum.PASSWORD && StringUtils.hasText(oauthUserId)) {
            Optional<MarketingUserDO> existingUser = userRepository.findByOAuthInfo(oauthUserId, authType.name());
            if (existingUser.isPresent()) {
                MarketingUserDO user = existingUser.get();
                // 如果用户想设置新的userUniq
                if (StringUtils.hasText(userUniq) && !userUniq.equals(user.getUserUniq())) {
                    throw new UserUniqExistsException("该OAuth账号已关联其他登录名");
                }
                // 更新OAuth token
                updateOAuthInfo(user, oauthAccessToken);
                user = userRepository.save(user);
                return createLoginResponse(user);
            }
        }

        String decryptedPassword = null;
        if (authType == AuthTypeEnum.PASSWORD) {
            try {
                decryptedPassword = rsaUtils.decrypt(encryptedPassword);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("密码解密失败，请重试");
            }
        }

        MarketingUserDO newUser = createNewUser(userUniq, decryptedPassword, oauthUserId, oauthAccessToken, authType);
        return createLoginResponse(newUser);
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

    private LoginResponse createLoginResponse(MarketingUserDO user) {
        LoginResponse response = new LoginResponse();
        response.setToken(jwtUtils.generateToken(user));
        response.setUserInfo(LoginResponse.UserInfo.fromDO(user));
        return response;
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
     * 检查用户名是否可用
     *
     * @param userUniq 用户名
     * @return 是否可用
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
    public Optional<MarketingUserDO> findById(String id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<MarketingUserDO> findByUserUniq(String userUniq) {
        return userRepository.findByUserUniq(userUniq);
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
    public List<MarketingUserDO> findByOrgId(String orgId) {
        return userRepository.findByOrgId(orgId);
    }

    public MarketingUserDO save(MarketingUserDO user) {
        return userRepository.save(user);
    }

    public void deleteById(String id) {
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
} 