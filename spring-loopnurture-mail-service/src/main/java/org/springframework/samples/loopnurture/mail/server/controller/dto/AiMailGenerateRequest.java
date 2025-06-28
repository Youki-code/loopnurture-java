package org.springframework.samples.loopnurture.mail.server.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 请求 DTO: 前端传入生成营销邮件所需的基本信息。
 */
@Data
public class AiMailGenerateRequest {

    /** 公司名称 */
    @NotBlank
    private String companyName;

    /** 邮件目的，例如 Promotion / On-Boarding */
    @NotBlank
    private String emailPurpose;

    /** 业务方描述的场景/需求 */
    @NotBlank
    private String requirement;
} 