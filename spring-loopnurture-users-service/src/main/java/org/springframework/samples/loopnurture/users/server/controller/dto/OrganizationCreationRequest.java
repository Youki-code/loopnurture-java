package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 组织创建请求DTO
 */
@Data
public class OrganizationCreationRequest {
    /**
     * 组织名称
     */
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    /**
     * 组织代码
     */
    @Pattern(regexp = "^[a-z0-9-]{3,50}$", message = "组织代码只能包含小写字母、数字和连字符，长度在3-50之间")
    private String code;

    /**
     * 组织描述
     */
    @Size(max = 500, message = "组织描述不能超过500个字符")
    private String description;

    /**
     * 是否为个人组织
     */
    private boolean personal = false;

    @Size(max = 200)
    private String address;

    private String phone;
    private String email;
    private String website;
    private String logoUrl;
} 