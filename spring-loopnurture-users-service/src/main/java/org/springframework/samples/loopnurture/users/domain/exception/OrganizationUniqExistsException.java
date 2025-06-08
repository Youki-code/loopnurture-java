package org.springframework.samples.loopnurture.users.domain.exception;

/**
 * 组织代码重复异常
 */
public class OrganizationUniqExistsException extends RuntimeException {
    public OrganizationUniqExistsException(String message) {
        super(message);
    }

    public OrganizationUniqExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 