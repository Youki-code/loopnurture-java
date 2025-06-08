package org.springframework.samples.loopnurture.users.infra.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.samples.loopnurture.users.domain.enums.UserRoleEnum;
import org.springframework.samples.loopnurture.users.domain.model.UserOrganizationDO;
import org.springframework.samples.loopnurture.users.infra.converter.UserOrganizationConverter;
import org.springframework.samples.loopnurture.users.infra.po.UserOrganizationPO;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(UserOrganizationConverter.class)
class UserOrganizationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserOrganizationConverter converter;

    @Autowired
    private UserOrganizationRepositoryImpl repository;

    private UserOrganizationPO testUserOrg;
    private Long testSystemUserId;
    private String testOrgId;

    @BeforeEach
    void setUp() {
        testSystemUserId = 1L;
        testOrgId = "test-org-id";

        testUserOrg = new UserOrganizationPO();
        testUserOrg.setSystemUserId(testSystemUserId);
        testUserOrg.setOrgId(testOrgId);
        testUserOrg.setRole(UserRoleEnum.MEMBER.getCode());
        testUserOrg.setCreatedAt(LocalDateTime.now());
        testUserOrg.setUpdatedAt(LocalDateTime.now());
        testUserOrg.setCreatedBy("test-user");
        testUserOrg.setUpdatedBy("test-user");

        entityManager.persist(testUserOrg);
        entityManager.flush();
    }

    @Test
    void findBySystemUserId_Success() {
        List<UserOrganizationDO> results = repository.findBySystemUserId(testSystemUserId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(testSystemUserId, results.get(0).getSystemUserId());
        assertEquals(testOrgId, results.get(0).getOrgId());
        assertEquals(UserRoleEnum.MEMBER, results.get(0).getRole());
    }

    @Test
    void findByOrgId_Success() {
        List<UserOrganizationDO> results = repository.findByOrgId(testOrgId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(testSystemUserId, results.get(0).getSystemUserId());
        assertEquals(testOrgId, results.get(0).getOrgId());
    }

    @Test
    void findBySystemUserIdAndOrgId_Success() {
        Optional<UserOrganizationDO> result = repository.findBySystemUserIdAndOrgId(testSystemUserId, testOrgId);

        assertTrue(result.isPresent());
        assertEquals(testSystemUserId, result.get().getSystemUserId());
        assertEquals(testOrgId, result.get().getOrgId());
    }

    @Test
    void findBySystemUserIdAndOrgId_NotFound() {
        Optional<UserOrganizationDO> result = repository.findBySystemUserIdAndOrgId(999L, "non-existent");

        assertTrue(result.isEmpty());
    }

    @Test
    void save_Success() {
        UserOrganizationDO newUserOrg = new UserOrganizationDO();
        newUserOrg.setSystemUserId(2L);
        newUserOrg.setOrgId("test-org-id-2");
        newUserOrg.setRole(UserRoleEnum.ADMIN);
        newUserOrg.setCreatedBy("test-user");
        newUserOrg.setUpdatedBy("test-user");

        UserOrganizationDO saved = repository.save(newUserOrg);

        assertNotNull(saved.getId());
        assertEquals(2L, saved.getSystemUserId());
        assertEquals("test-org-id-2", saved.getOrgId());
        assertEquals(UserRoleEnum.ADMIN, saved.getRole());
    }

    @Test
    void deleteBySystemUserIdAndOrgId_Success() {
        repository.deleteBySystemUserIdAndOrgId(testSystemUserId, testOrgId);

        Optional<UserOrganizationPO> deleted = Optional.ofNullable(
            entityManager.find(UserOrganizationPO.class, testUserOrg.getId())
        );
        assertTrue(deleted.isEmpty());
    }

    @Test
    void deleteBySystemUserId_Success() {
        repository.deleteBySystemUserId(testSystemUserId);

        List<UserOrganizationDO> remaining = repository.findBySystemUserId(testSystemUserId);
        assertTrue(remaining.isEmpty());
    }

    @Test
    void deleteByOrgId_Success() {
        repository.deleteByOrgId(testOrgId);

        List<UserOrganizationDO> remaining = repository.findByOrgId(testOrgId);
        assertTrue(remaining.isEmpty());
    }

    @Test
    void existsBySystemUserIdAndOrgId_True() {
        boolean exists = repository.existsBySystemUserIdAndOrgId(testSystemUserId, testOrgId);
        assertTrue(exists);
    }

    @Test
    void existsBySystemUserIdAndOrgId_False() {
        boolean exists = repository.existsBySystemUserIdAndOrgId(999L, "non-existent");
        assertFalse(exists);
    }
} 