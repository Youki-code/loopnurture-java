package org.springframework.samples.loopnurture.mail.exception;

/**
 * 邮件发送异常
 */
public class MailSendException extends MailBusinessException {
    
    public MailSendException(String message) {
        super(message);
    }

    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MailSendException invalidEmailAddress(String email) {
        return new MailSendException("Invalid email address: " + email);
    }

    public static MailSendException templateRenderError(String templateId, Throwable cause) {
        return new MailSendException("Failed to render template: " + templateId, cause);
    }

    public static MailSendException sendError(String logId, Throwable cause) {
        return new MailSendException("Failed to send email: " + logId, cause);
    }
} 