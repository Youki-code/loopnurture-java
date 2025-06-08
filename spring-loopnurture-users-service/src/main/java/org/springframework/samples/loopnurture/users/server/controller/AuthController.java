package org.springframework.samples.loopnurture.users.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.loopnurture.users.service.MarketingUserService;
import org.springframework.samples.loopnurture.users.infra.utils.RsaUtils;
import org.springframework.samples.loopnurture.users.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.users.server.controller.dto.LoginRequest;
import org.springframework.samples.loopnurture.users.server.controller.dto.LoginResponse;
import org.springframework.samples.loopnurture.users.server.controller.dto.TokenValidationResponse;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;

import jakarta.validation.Valid;

/**
 * 认证接口
 */
@Tag(name = "认证接口", description = "包括登录、令牌验证等认证相关接口")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MarketingUserService userService;
    private final RsaUtils rsaUtils;

    /**
     * 获取RSA公钥
     * 前端使用该公钥对密码进行加密
     */
    @Operation(summary = "获取RSA公钥", description = "前端使用该公钥对密码进行加密")
    @GetMapping("/public-key")
    public ApiResponse<String> getPublicKey() {
        return ApiResponse.ok(rsaUtils.getPublicKey());
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录结果
     */
    @Operation(summary = "用户登录", description = "支持密码登录和OAuth登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(userService.login(request));
    }

    /**
     * 验证JWT令牌
     * 供其他服务验证用户令牌的有效性
     *
     * @param token 令牌
     * @return 验证结果，包含用户信息
     */
    @Operation(summary = "验证JWT令牌", description = "供其他服务验证用户令牌的有效性")
    @PostMapping("/validate")
    public ApiResponse<TokenValidationResponse> validateToken(@RequestParam String token) {
        return ApiResponse.ok(userService.validateToken(token));
    }

    /**
     * 登录示例：
     * {
     *   "authType": "PASSWORD",
     *   "userUniq": "testuser",
     *   "password": "RSA加密后的密码"
     * }
     * 
     * OAuth登录示例：
     * {
     *   "authType": "GOOGLE_OAUTH",
     *   "oauthUserId": "google_123456",
     *   "oauthAccessToken": "google_token_xxx"
     * }
     */
} 