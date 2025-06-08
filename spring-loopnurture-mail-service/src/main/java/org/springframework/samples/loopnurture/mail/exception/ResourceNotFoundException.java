package org.springframework.samples.loopnurture.mail.exception;

/**
 * 资源不存在异常
 */
public class ResourceNotFoundException extends MailBusinessException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException templateNotFound(String templateId) {
        return new ResourceNotFoundException("Template not found: " + templateId);
    }

    public static ResourceNotFoundException ruleNotFound(String ruleId) {
        return new ResourceNotFoundException("Rule not found: " + ruleId);
    }

    public static ResourceNotFoundException sendLogNotFound(String logId) {
        return new ResourceNotFoundException("Send log not found: " + logId);
    }
} 