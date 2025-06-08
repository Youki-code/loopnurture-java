package org.springframework.samples.loopnurture.users.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.service.MarketingUserService;
import org.springframework.samples.loopnurture.users.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.users.server.controller.dto.RegisterRequest;
import org.springframework.samples.loopnurture.users.server.controller.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户管理接口
 */
@Tag(name = "用户管理接口", description = "包括用户注册、信息查询等用户管理相关接口")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final MarketingUserService userService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "支持密码注册和OAuth注册")
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        MarketingUserDO user = userService.register(request);
        return ApiResponse.ok(UserResponse.fromDO(user));
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        MarketingUserDO user = userService.findByUserUniq(id);
        return ApiResponse.ok(UserResponse.fromDO(user));
    }
} 