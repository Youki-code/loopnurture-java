package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 响应 DTO: 返回 AI 生成的邮件主题与 HTML 内容。
 */
@Data
@Builder
public class AiMailGenerateResponse {

    /** 邮件主题 */
    private String subject;

    /** 完整 HTML 内容 */
    private String html;
} 