package org.springframework.samples.loopnurture.users.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.loopnurture.users.service.OrganizationService;
import org.springframework.samples.loopnurture.users.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.users.server.controller.dto.OrganizationDTO;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.infra.context.UserContext;

import java.util.List;

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
    public ApiResponse<List<OrganizationDTO>> getMyOrganizations() {
        List<OrganizationDTO> organizations = organizationService.getOrganizationsByUserId(UserContext.getCurrentUserId())
            .stream()
            .map(this::convertToDTO)
            .toList();
        return ApiResponse.ok(organizations);
    }

    /**
     * 获取组织详情
     */
    @GetMapping("/{id}")
    public ApiResponse<OrganizationDTO> getOrganization(@PathVariable("id") String id) {
        OrganizationDO organization = organizationService.findById(id);
        return ApiResponse.ok(convertToDTO(organization));
    }

    @GetMapping
    public ApiResponse<List<OrganizationDTO>> listOrganizations() {
        List<OrganizationDTO> organizations = organizationService.findAll()
            .stream()
            .map(this::convertToDTO)
            .toList();
        return ApiResponse.ok(organizations);
    }

    private OrganizationDTO convertToDTO(OrganizationDO organization) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setOrgId(organization.getOrgId());
        dto.setOrgName(organization.getOrgName());
        dto.setDescription(organization.getDescription());
        dto.setStatus(organization.getStatus() != null ? organization.getStatus().getCode() : null);
        dto.setOrgType(organization.getOrgType() != null ? organization.getOrgType().getCode() : null);
        dto.setCreatedAt(organization.getCreatedAt());
        dto.setUpdatedAt(organization.getUpdatedAt());
        return dto;
    }
} 