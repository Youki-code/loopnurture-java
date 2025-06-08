package org.springframework.samples.loopnurture.mail.exception;

/**
 * 邮件服务基础业务异常
 */
public class MailBusinessException extends RuntimeException {
    
    public MailBusinessException(String message) {
        super(message);
    }

    public MailBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 