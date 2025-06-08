package org.springframework.samples.loopnurture.users.domain.exception;

/**
 * 组织不存在异常
 */
public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException(String message) {
        super(message);
    }

    public OrganizationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 