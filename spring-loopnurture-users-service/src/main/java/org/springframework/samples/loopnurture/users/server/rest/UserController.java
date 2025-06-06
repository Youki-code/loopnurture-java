package org.springframework.samples.loopnurture.users.server.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.users.server.dto.*;
import org.springframework.samples.loopnurture.users.service.UserService;
import org.springframework.samples.loopnurture.users.infra.utils.JwtUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;

/**
 * 用户管理接口
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * 注册用户
     * 如果未提供组织ID，将自动创建一个默认的个人组织
     *
     * @param request 注册请求
     * @return 注册成功的用户信息，包含组织信息
     */
    @PostMapping("/register")
    public ApiResponse<UserRegistrationResult> registerUser(@Validated @RequestBody UserRegistrationRequest request) {
        UserRegistrationResult result = userService.registerUser(request);
        return ApiResponse.success(result);
    }

    /**
     * 根据用户唯一标识查询用户详情
     *
     * @param userUniq 用户唯一标识（用户名/邮箱）
     * @return 用户详细信息
     */
    @GetMapping("/{userUniq}")
    public ApiResponse<UserResponse> getUserByUniq(@PathVariable String userUniq) {
        UserResponse user = userService.getUserByUniq(userUniq);
        return ApiResponse.success(user);
    }

    /**
     * 根据OAuth用户ID查询用户详情
     *
     * @param authType OAuth认证类型
     * @param oauthUserId OAuth用户ID
     * @return 用户详细信息
     */
    @GetMapping("/oauth/{authType}/{oauthUserId}")
    public ApiResponse<UserResponse> getUserByOAuth(
        @PathVariable String authType,
        @PathVariable String oauthUserId
    ) {
        UserResponse user = userService.getUserByOAuth(authType, oauthUserId);
        return ApiResponse.success(user);
    }

    /**
     * 验证token并返回用户信息
     *
     * @param token JWT token
     * @return 用户信息
     */
    @PostMapping("/validate-token")
    public ApiResponse<UserInfo> validateToken(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token
        if (!jwtUtils.validateToken(token)) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        // 获取token中的用户信息
        Claims claims = jwtUtils.getClaimsFromToken(token);
        
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(claims.getSubject());
        userInfo.setUsername((String) claims.get("userUniq"));
        userInfo.setOrgId((String) claims.get("organizationId"));
        userInfo.setOrgName((String) claims.get("organizationName"));
        
        return ApiResponse.success(userInfo);
    }
} 