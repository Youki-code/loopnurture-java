package org.springframework.samples.loopnurture.users.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.repository.OrganizationRepository;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationTypeEnum;
import org.springframework.samples.loopnurture.users.domain.exception.OrganizationNotFoundException;
import org.springframework.samples.loopnurture.users.domain.exception.OrganizationUniqExistsException;
import org.springframework.samples.loopnurture.users.config.TestConfig;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class OrganizationServiceTest {

    @Autowired
    private OrganizationService organizationService;

    @MockBean
    private OrganizationRepository organizationRepository;

    private OrganizationDO testOrg;

    @BeforeEach
    void setUp() {
        testOrg = new OrganizationDO();
        testOrg.setOrgCode("TEST_ORG");
        testOrg.setOrgName("Test Organization");
        testOrg.setOrgType(OrganizationTypeEnum.ENTERPRISE);
        testOrg.setStatus(OrganizationStatusEnum.ACTIVE);
        testOrg.setMaxUsers(100);
        testOrg.setMaxTemplates(200);
        testOrg.setMaxRules(50);
    }

    @Test
    void createOrganization_Success() {
        when(organizationRepository.existsByOrgCode(anyString())).thenReturn(false);
        when(organizationRepository.save(any(OrganizationDO.class))).thenReturn(testOrg);

        OrganizationDO result = organizationService.createOrganization(testOrg);

        assertNotNull(result);
        assertEquals(testOrg.getOrgCode(), result.getOrgCode());
        verify(organizationRepository).save(any(OrganizationDO.class));
    }

    @Test
    void createOrganization_DuplicateOrgCode() {
        when(organizationRepository.existsByOrgCode(anyString())).thenReturn(true);

        assertThrows(OrganizationUniqExistsException.class, () -> 
            organizationService.createOrganization(testOrg)
        );
    }

    @Test
    void findByOrgCode_Success_Service() {
        when(organizationRepository.findByOrgCode("TEST_ORG")).thenReturn(testOrg);

        OrganizationDO result = organizationService.findByOrgCode("TEST_ORG");

        assertNotNull(result);
        assertEquals(testOrg.getOrgCode(), result.getOrgCode());
    }

    @Test
    void findByOrgCode_NotFound_Service() {
        when(organizationRepository.findByOrgCode(anyString())).thenReturn(null);
        assertThrows(OrganizationNotFoundException.class, () ->
            organizationService.findByOrgCode("NONEXISTENT")
        );
    }

    @Test
    void findAll_Success() {
        OrganizationDO org2 = new OrganizationDO();
        org2.setOrgCode("TEST_ORG_2");
        
        List<OrganizationDO> orgs = Arrays.asList(testOrg, org2);
        when(organizationRepository.findAll()).thenReturn(orgs);

        List<OrganizationDO> results = organizationService.findAll();

        assertEquals(2, results.size());
        assertEquals(testOrg.getOrgCode(), results.get(0).getOrgCode());
        assertEquals(org2.getOrgCode(), results.get(1).getOrgCode());
    }

    @Test
    void updateOrganization_Success() {
        when(organizationRepository.findByOrgCode("TEST_ORG")).thenReturn(testOrg);
        when(organizationRepository.save(any(OrganizationDO.class))).thenReturn(testOrg);

        testOrg.setOrgName("Updated Organization");
        OrganizationDO result = organizationService.updateOrganization("TEST_ORG", testOrg);

        assertNotNull(result);
        assertEquals("Updated Organization", result.getOrgName());
        verify(organizationRepository).save(any(OrganizationDO.class));
    }

    @Test
    void updateOrganizationStatus_Success() {
        when(organizationRepository.findByOrgCode("TEST_ORG")).thenReturn(testOrg);
        when(organizationRepository.save(any(OrganizationDO.class))).thenReturn(testOrg);

        OrganizationDO result = organizationService.updateOrganizationStatus("TEST_ORG", OrganizationStatusEnum.DISABLED);

        assertNotNull(result);
        assertEquals(OrganizationStatusEnum.DISABLED, result.getStatus());
        verify(organizationRepository).save(any(OrganizationDO.class));
    }

    @Test
    void updateOrganizationSettings_Success() {
        when(organizationRepository.findByOrgCode("TEST_ORG")).thenReturn(testOrg);
        when(organizationRepository.save(any(OrganizationDO.class))).thenReturn(testOrg);

        testOrg.setMaxUsers(200);
        testOrg.setMaxTemplates(300);
        testOrg.setMaxRules(100);

        OrganizationDO result = organizationService.updateOrganizationSettings("TEST_ORG", testOrg);

        assertNotNull(result);
        assertEquals(200, result.getMaxUsers());
        assertEquals(300, result.getMaxTemplates());
        assertEquals(100, result.getMaxRules());
        verify(organizationRepository).save(any(OrganizationDO.class));
    }

    @Test
    void deleteOrganization_Success() {
        when(organizationRepository.findByOrgCode("TEST_ORG")).thenReturn(testOrg);
        doNothing().when(organizationRepository).deleteByOrgCode(anyString());

        organizationService.deleteOrganization("TEST_ORG");

        verify(organizationRepository).deleteByOrgCode("TEST_ORG");
    }

    @Test
    void deleteOrganization_NotFound() {
        when(organizationRepository.findByOrgCode(anyString())).thenReturn(null);

        assertThrows(OrganizationNotFoundException.class, () -> 
            organizationService.deleteOrganization("NONEXISTENT")
        );
    }
} 