package org.springframework.samples.loopnurture.users.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;

/**
 * 登录响应DTO
 */
@Data
@Schema(description = "登录响应")
public class LoginResponse {
    /**
     * JWT令牌
     */
    @Schema(description = "JWT令牌", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;
    
    /**
     * 用户信息
     */
    @Schema(description = "用户信息")
    private UserInfo userInfo;
    
    @Data
    @Schema(description = "用户基本信息")
    public static class UserInfo {
        @Schema(description = "系统用户ID", example = "123456")
        private Long systemUserId;

        @Schema(description = "用户登录名", example = "testuser")
        private String userUniq;

        @Schema(description = "认证类型", example = "PASSWORD")
        private String authType;

        @Schema(description = "主邮箱", example = "test@example.com")
        private String primaryEmail;

        @Schema(description = "手机号", example = "1234567890")
        private String phone;
        
        public static UserInfo fromDO(MarketingUserDO user) {
            UserInfo info = new UserInfo();
            info.setSystemUserId(user.getSystemUserId());
            info.setUserUniq(user.getUserUniq());
            info.setAuthType(user.getAuthType().name());
            info.setPrimaryEmail(user.getPrimaryEmail());
            info.setPhone(user.getPhone());
            return info;
        }
    }
} 