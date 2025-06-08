package org.springframework.samples.loopnurture.mail.server.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import lombok.Data;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "user-service", path = "/api/v1")
public interface UserServiceClient {
    
    /**
     * 验证用户Token
     */
    @PostMapping("/auth/validate")
    TokenValidationResponse validateToken(@RequestHeader("Authorization") String token);
}

/**
 * Token验证响应
 */
@Data
class TokenValidationResponse {
    private String userId;
    private String orgId;
    private String username;
    private boolean valid;
} 