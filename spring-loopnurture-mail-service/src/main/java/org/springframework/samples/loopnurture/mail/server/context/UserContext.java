package org.springframework.samples.loopnurture.mail.server.context;

import lombok.Data;

/**
 * 用户上下文信息
 */
@Data
public class UserContext {
    private String userId;
    private String username;
    private String orgId;
    private String orgName;

    private static final ThreadLocal<UserContext> userHolder = new ThreadLocal<>();

    public static void setUser(UserContext user) {
        userHolder.set(user);
    }

    public static UserContext getUser() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }
} 