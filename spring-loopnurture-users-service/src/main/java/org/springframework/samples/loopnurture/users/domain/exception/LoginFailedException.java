package org.springframework.samples.loopnurture.users.domain.exception;

/**
 * 登录失败异常
 */
public class LoginFailedException extends RuntimeException {
    public LoginFailedException(String message) {
        super(message);
    }
} 