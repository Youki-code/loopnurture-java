package org.springframework.samples.loopnurture.users.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.samples.loopnurture.users.domain.enums.UserRoleEnum;
import org.springframework.samples.loopnurture.users.domain.model.UserOrganizationDO;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.UserOrganizationRepository;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.domain.repository.MarketingUserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserOrganizationServiceTest {

    @MockBean
    private UserOrganizationRepository userOrgRepository;

    @MockBean
    private OrganizationRepository organizationRepository;

    @MockBean
    private MarketingUserRepository userRepository;

    private UserOrganizationService userOrgService;

    private UserOrganizationDO testUserOrg;
    private Long testSystemUserId;
    private String testOrgId;

    @BeforeEach
    void setUp() {
        userOrgService = new UserOrganizationService(
            userOrgRepository,
            organizationRepository,
            userRepository
        );

        testSystemUserId = 1L;
        testOrgId = "test-org-id";

        testUserOrg = new UserOrganizationDO();
        testUserOrg.setId(1L);
        testUserOrg.setSystemUserId(testSystemUserId);
        testUserOrg.setOrgId(testOrgId);
        testUserOrg.setRole(UserRoleEnum.MEMBER);
        testUserOrg.setCreatedAt(LocalDateTime.now());
        testUserOrg.setUpdatedAt(LocalDateTime.now());
        testUserOrg.setCreatedBy("test-user");
        testUserOrg.setUpdatedBy("test-user");
    }

    @Test
    void addUserToOrganization_Success() {
        when(userRepository.findBySystemUserId(anyLong())).thenReturn(Optional.of(new MarketingUserDO()));
        when(organizationRepository.findById(anyString())).thenReturn(Optional.of(new OrganizationDO()));
        when(userOrgRepository.existsBySystemUserIdAndOrgId(anyLong(), anyString())).thenReturn(false);
        when(userOrgRepository.save(any(UserOrganizationDO.class))).thenReturn(testUserOrg);

        UserOrganizationDO result = userOrgService.addUserToOrganization(
            testSystemUserId,
            testOrgId,
            UserRoleEnum.MEMBER,
            "test-user"
        );

        assertNotNull(result);
        assertEquals(testSystemUserId, result.getSystemUserId());
        assertEquals(testOrgId, result.getOrgId());
        assertEquals(UserRoleEnum.MEMBER, result.getRole());
        verify(userOrgRepository).save(any(UserOrganizationDO.class));
    }

    @Test
    void addUserToOrganization_AlreadyExists() {
        when(userOrgRepository.existsBySystemUserIdAndOrgId(anyLong(), anyString())).thenReturn(true);

        assertThrows(IllegalStateException.class, () ->
            userOrgService.addUserToOrganization(
                testSystemUserId,
                testOrgId,
                UserRoleEnum.MEMBER,
                "test-user"
            )
        );
        verify(userOrgRepository, never()).save(any(UserOrganizationDO.class));
    }

    @Test
    void updateUserRole_Success() {
        when(userOrgRepository.findBySystemUserIdAndOrgId(anyLong(), anyString()))
            .thenReturn(Optional.of(testUserOrg));
        when(userOrgRepository.save(any(UserOrganizationDO.class))).thenReturn(testUserOrg);

        UserOrganizationDO result = userOrgService.updateUserRole(
            testSystemUserId,
            testOrgId,
            UserRoleEnum.ADMIN,
            "test-user"
        );

        assertNotNull(result);
        assertEquals(UserRoleEnum.ADMIN, result.getRole());
        verify(userOrgRepository).save(any(UserOrganizationDO.class));
    }

    @Test
    void updateUserRole_NotFound() {
        when(userOrgRepository.findBySystemUserIdAndOrgId(anyLong(), anyString()))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
            userOrgService.updateUserRole(
                testSystemUserId,
                testOrgId,
                UserRoleEnum.ADMIN,
                "test-user"
            )
        );
        verify(userOrgRepository, never()).save(any(UserOrganizationDO.class));
    }

    @Test
    void removeUserFromOrganization_Success() {
        when(userOrgRepository.findBySystemUserIdAndOrgId(anyLong(), anyString()))
            .thenReturn(Optional.of(testUserOrg));
        doNothing().when(userOrgRepository).deleteBySystemUserIdAndOrgId(anyLong(), anyString());

        userOrgService.removeUserFromOrganization(testSystemUserId, testOrgId);

        verify(userOrgRepository).deleteBySystemUserIdAndOrgId(testSystemUserId, testOrgId);
    }

    @Test
    void removeUserFromOrganization_NotFound() {
        when(userOrgRepository.findBySystemUserIdAndOrgId(anyLong(), anyString()))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
            userOrgService.removeUserFromOrganization(testSystemUserId, testOrgId)
        );
        verify(userOrgRepository, never()).deleteBySystemUserIdAndOrgId(anyLong(), anyString());
    }

    @Test
    void getUserOrganizations_Success() {
        UserOrganizationDO org2 = new UserOrganizationDO();
        org2.setSystemUserId(testSystemUserId);
        org2.setOrgId("test-org-id-2");
        org2.setRole(UserRoleEnum.VIEWER);

        when(userOrgRepository.findBySystemUserId(anyLong()))
            .thenReturn(Arrays.asList(testUserOrg, org2));

        List<UserOrganizationDO> results = userOrgService.getUserOrganizations(testSystemUserId);

        assertEquals(2, results.size());
        assertEquals(testOrgId, results.get(0).getOrgId());
        assertEquals("test-org-id-2", results.get(1).getOrgId());
    }

    @Test
    void getOrganizationUsers_Success() {
        UserOrganizationDO user2 = new UserOrganizationDO();
        user2.setSystemUserId(2L);
        user2.setOrgId(testOrgId);
        user2.setRole(UserRoleEnum.VIEWER);

        when(userOrgRepository.findByOrgId(anyString()))
            .thenReturn(Arrays.asList(testUserOrg, user2));

        List<UserOrganizationDO> results = userOrgService.getOrganizationUsers(testOrgId);

        assertEquals(2, results.size());
        assertEquals(testSystemUserId, results.get(0).getSystemUserId());
        assertEquals(2L, results.get(1).getSystemUserId());
    }

    @Test
    void getUserRole_Success() {
        when(userOrgRepository.findBySystemUserIdAndOrgId(anyLong(), anyString()))
            .thenReturn(Optional.of(testUserOrg));

        Optional<UserRoleEnum> result = userOrgService.getUserRole(testSystemUserId, testOrgId);

        assertTrue(result.isPresent());
        assertEquals(UserRoleEnum.MEMBER, result.get());
    }

    @Test
    void getUserRole_NotFound() {
        when(userOrgRepository.findBySystemUserIdAndOrgId(anyLong(), anyString()))
            .thenReturn(Optional.empty());

        Optional<UserRoleEnum> result = userOrgService.getUserRole(testSystemUserId, testOrgId);

        assertTrue(result.isEmpty());
    }
} 