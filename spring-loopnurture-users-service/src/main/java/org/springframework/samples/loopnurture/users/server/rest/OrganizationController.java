package org.springframework.samples.loopnurture.users.server.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.infra.context.UserContext;
import org.springframework.samples.loopnurture.users.server.annotation.RequireLogin;
import org.springframework.samples.loopnurture.users.server.dto.ApiResponse;
import org.springframework.samples.loopnurture.users.server.dto.OrganizationDTO;
import org.springframework.samples.loopnurture.users.service.OrganizationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织管理接口
 */
@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    /**
     * 获取当前用户的组织列表
     */
    @GetMapping("/my")
    @RequireLogin
    public ApiResponse<List<OrganizationDTO>> getMyOrganizations() {
        Long currentUserId = UserContext.getCurrentUserId();
        List<OrganizationDO> organizations = organizationService.getOrganizationsByUserId(currentUserId);
        List<OrganizationDTO> dtos = organizations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ApiResponse.ok(dtos);
    }

    /**
     * 获取组织详情
     */
    @GetMapping("/{orgId}")
    @RequireLogin
    public ApiResponse<OrganizationDTO> getOrganizationDetail(@PathVariable String orgId) {
        OrganizationDO organization = organizationService.getOrganizationById(orgId);
        return ApiResponse.ok(convertToDTO(organization));
    }

    /**
     * 将DO转换为DTO
     */
    private OrganizationDTO convertToDTO(OrganizationDO organization) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setOrgId(organization.getOrgId());
        dto.setOrgName(organization.getOrgName());
        dto.setDescription(organization.getDescription());
        dto.setStatus(organization.getStatus());
        dto.setOrgType(organization.getOrgType());
        dto.setCreatedAt(organization.getCreatedAt());
        dto.setUpdatedAt(organization.getUpdatedAt());
        return dto;
    }
} 