package org.springframework.samples.loopnurture.users.infra.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.repository.MarketingUserRepository;

@SpringBootTest
@ActiveProfiles("remote")
class MarketingUserRepositoryIntegrationTest {

    @Autowired
    private MarketingUserRepository marketingUserRepository;

    /**
     * 简单验证：根据已知的用户 oauthUserId & authType 能否查询到数据。
     * 测试前请确保远程库中存在对应记录，否则断言可改为 isEmpty().
     */
    @Test
    void testFindByOAuthInfo() {
        Assertions.assertDoesNotThrow(() ->
            marketingUserRepository.findByOAuthInfo("non_exist", AuthTypeEnum.GOOGLE_OAUTH.getCode()),
            "无法连接远程数据库或查询时报错");
    }
} 