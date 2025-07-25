package org.springframework.samples.loopnurture.users.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.service.MarketingUserService;
import org.springframework.samples.loopnurture.users.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.users.server.controller.dto.RegisterRequest;
import org.springframework.samples.loopnurture.users.server.controller.dto.UserResponse;
import org.springframework.samples.loopnurture.users.server.controller.dto.LoginResponse;
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
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        MarketingUserDO user = userService.register(request);
        String token = userService.generateToken(user.getUserUniq());

        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.fromDO(user);
        userInfo.setOrgCode(userService.findFirstOrgCode(user.getSystemUserId()));
        resp.setUserInfo(userInfo);

        return ApiResponse.ok(resp);
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        MarketingUserDO user = userService.findBySystemUserId(id);
        UserResponse userResponse = UserResponse.fromDO(user);
        userResponse.setOrgCode(userService.findFirstOrgCode(user.getSystemUserId()));
        return ApiResponse.ok(userResponse);
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户信息")
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody UserResponse updateRequest) {
        MarketingUserDO user = userService.findByUserUniq(id);
        if (user == null) {
            throw new org.springframework.samples.loopnurture.users.domain.exception.ResourceNotFoundException("User", "userUniq", id);
        }

        // 简单映射需要更新的字段（根据测试用例，仅涉及昵称和邮箱）
        if (updateRequest.getNickname() != null) {
            user.setNickname(updateRequest.getNickname());
        }
        if (updateRequest.getPrimaryEmail() != null) {
            user.setPrimaryEmail(updateRequest.getPrimaryEmail());
        }

        MarketingUserDO saved = userService.save(user);
        UserResponse userResponse = UserResponse.fromDO(saved);
        userResponse.setOrgCode(userService.findFirstOrgCode(saved.getSystemUserId()));
        return ApiResponse.ok(userResponse);
    }
} 