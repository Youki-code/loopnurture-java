package org.springframework.samples.loopnurture.users.domain.repository;

import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import java.util.List;
import java.util.Optional;

/**
 * 营销用户仓储接口
 */
public interface MarketingUserRepository {
    /**
     * 根据系统用户ID查询用户
     *
     * @param systemUserId 系统用户ID
     * @return 用户信息（如果存在）
     */
    Optional<MarketingUserDO> findBySystemUserId(Long systemUserId);

    /**
     * 根据OAuth信息查询用户
     *
     * @param oauthUserId OAuth用户ID
     * @param authType 认证类型
     * @return 用户信息（如果存在）
     */
    Optional<MarketingUserDO> findByOAuthInfo(String oauthUserId, String authType);

    /**
     * 根据用户唯一标识查询用户
     *
     * @param userUniq 用户唯一标识
     * @return 用户信息（如果存在）
     */
    Optional<MarketingUserDO> findByUserUniq(String userUniq);

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 保存后的用户信息
     */
    MarketingUserDO save(MarketingUserDO user);

    /**
     * 检查用户唯一标识是否已存在
     *
     * @param userUniq 用户唯一标识
     * @return 是否存在
     */
    boolean existsByUserUniq(String userUniq);

    List<MarketingUserDO> findAll();
    Optional<MarketingUserDO> findById(String id);
    Optional<MarketingUserDO> findByPrimaryEmail(String primaryEmail);
    Optional<MarketingUserDO> findByPhone(String phone);
    List<MarketingUserDO> findByOrgCode(String orgCode);
    void deleteById(String id);
    boolean existsByPrimaryEmail(String primaryEmail);
    boolean existsByPhone(String phone);
} 