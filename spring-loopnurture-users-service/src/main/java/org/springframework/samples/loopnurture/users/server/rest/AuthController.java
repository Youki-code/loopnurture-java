package org.springframework.samples.loopnurture.users.server.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.users.infra.utils.RsaUtils;
import org.springframework.samples.loopnurture.users.server.dto.ApiResponse;
import org.springframework.samples.loopnurture.users.server.dto.LoginRequest;
import org.springframework.samples.loopnurture.users.server.dto.LoginResponse;
import org.springframework.samples.loopnurture.users.server.dto.RegisterRequest;
import org.springframework.samples.loopnurture.users.service.MarketingUserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证接口
 */
@Tag(name = "认证接口", description = "包括登录、注册等认证相关接口")
@RestController
@RequestMapping("/api/auth")
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
    public ApiResponse<Map<String, String>> getPublicKey() {
        Map<String, String> result = new HashMap<>();
        result.put("publicKey", rsaUtils.getPublicKey());
        return ApiResponse.success(result);
    }

    /**
     * 用户注册
     * 支持两种注册方式：
     * 1. 密码注册：需要提供userUniq（可选）和encryptedPassword（RSA加密后的密码）
     * 2. OAuth注册：需要提供userUniq（可选）、oauthUserId和oauthAccessToken
     */
    @Operation(summary = "用户注册", description = "支持密码注册和OAuth注册")
    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@RequestBody @Valid RegisterRequest request) {
        request.validate();
        LoginResponse response = userService.registerUser(
            request.getUserUniq(),
            request.getEncryptedPassword(),
            request.getOauthUserId(),
            request.getOauthAccessToken(),
            request.getAuthType()
        );
        return ApiResponse.success(response);
    }

    /**
     * 用户登录
     * 支持两种登录方式：
     * 1. 密码登录：需要提供userUniq和password（使用RSA公钥加密后的密码）
     * 2. OAuth登录：需要提供oauthUserId和oauthAccessToken
     */
    @Operation(summary = "用户登录", description = "支持密码登录和OAuth登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ApiResponse.success(response);
    }

    /**
     * 密码注册示例：
     * {
     *   "authType": "PASSWORD",
     *   "userUniq": "testuser",
     *   "encryptedPassword": "RSA加密后的密码"
     * }
     *
     * OAuth注册示例：
     * {
     *   "authType": "GOOGLE_OAUTH",
     *   "userUniq": "testuser",
     *   "oauthUserId": "google_123456",
     *   "oauthAccessToken": "google_token_xxx"
     * }
     */
} 