package org.springframework.samples.loopnurture.users.infra.context;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;

/**
 * 用户上下文
 * 使用ThreadLocal存储当前请求的用户信息
 */
@Data
public class UserContext {
    private static final ThreadLocal<UserContext> contextHolder = new ThreadLocal<>();

    /**
     * 系统用户ID
     */
    private Long systemUserId;

    /**
     * 用户登录名
     */
    private String userUniq;

    /**
     * 认证类型
     */
    private AuthTypeEnum authType;

    /**
     * 当前选择的组织ID
     */
    private String organizationId;

    /**
     * 设置用户上下文
     */
    public static void set(UserContext context) {
        contextHolder.set(context);
    }

    /**
     * 获取用户上下文
     */
    public static UserContext get() {
        return contextHolder.get();
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        UserContext context = get();
        return context != null ? context.getSystemUserId() : null;
    }

    /**
     * 获取当前组织ID
     */
    public static String getCurrentOrganizationId() {
        UserContext context = get();
        return context != null ? context.getOrganizationId() : null;
    }

    /**
     * 清除用户上下文
     */
    public static void clear() {
        contextHolder.remove();
    }
} 