package org.springframework.samples.loopnurture.mail.server.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.samples.loopnurture.mail.server.feign.dto.UserInfo;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "user-service", url = "${services.user.url}")
public interface UserServiceClient {

    /**
     * 验证token并获取用户信息
     */
    @PostMapping("/api/v1/users/validate-token")
    UserInfo validateToken(@RequestHeader("Authorization") String token);
} 