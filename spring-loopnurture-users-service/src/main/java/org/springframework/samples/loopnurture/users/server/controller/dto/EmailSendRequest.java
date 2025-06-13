package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;
import java.util.Map;

/**
 * 邮件发送请求
 */
@Data
public class EmailSendRequest {
    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 收件人邮箱
     */
    private String toEmail;

    /**
     * 收件人姓名
     */
    private String toName;

    /**
     * 模板参数
     */
    private Map<String, Object> parameters;
} 