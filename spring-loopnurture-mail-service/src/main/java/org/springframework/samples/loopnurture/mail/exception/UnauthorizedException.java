package org.springframework.samples.loopnurture.mail.exception;

/**
 * 未授权异常
 */
public class UnauthorizedException extends MailBusinessException {
    
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
} 