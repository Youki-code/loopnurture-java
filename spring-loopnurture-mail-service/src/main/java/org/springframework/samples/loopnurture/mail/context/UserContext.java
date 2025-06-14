package org.springframework.samples.loopnurture.mail.context;

import lombok.Data;

/**
 * 用户上下文（Mail 模块）
 * 通过 ThreadLocal 在 AOP/过滤器中注入，业务代码可随处静态获取。
 */
@Data
public class UserContext {

    private static final ThreadLocal<UserContext> HOLDER = new ThreadLocal<>();

    private String userId;
    private String username;
    private String orgCode;
    private String orgName;
    private String token;

    /** 当上下文不存在时代表系统调用用户ID */
    private static final String SYSTEM_USER_ID = "SYSTEM";

    /* ---------- 静态便捷方法 ---------- */

    public static void set(UserContext ctx) {
        HOLDER.set(ctx);
    }

    public static UserContext get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    /** 当前用户 ID */
    public static String getUserId() {
        UserContext c = get();
        return c != null ? c.getUserId() : SYSTEM_USER_ID;
    }

    /** 当前组织编码 */
    public static String getOrgCode() {
        UserContext c = get();
        return c != null ? c.getOrgCode() : null;
    }
} 