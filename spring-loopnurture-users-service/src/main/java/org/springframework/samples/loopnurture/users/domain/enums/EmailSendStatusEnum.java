package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 邮件发送状态枚举
 */
public enum EmailSendStatusEnum {
    /**
     * 待发送
     */
    PENDING,

    /**
     * 发送中
     */
    SENDING,

    /**
     * 发送成功
     */
    SUCCESS,

    /**
     * 发送失败
     */
    FAILED,

    /**
     * 已取消
     */
    CANCELLED
} 