package org.springframework.samples.loopnurture.users.domain.exception;

/**
 * 用户唯一标识已存在异常
 */
public class UserUniqExistsException extends RuntimeException {
    private final String userUniq;

    public UserUniqExistsException(String userUniq) {
        super(String.format("登录名 '%s' 已经存在，请更换", userUniq));
        this.userUniq = userUniq;
    }

    public String getUserUniq() {
        return userUniq;
    }
} 