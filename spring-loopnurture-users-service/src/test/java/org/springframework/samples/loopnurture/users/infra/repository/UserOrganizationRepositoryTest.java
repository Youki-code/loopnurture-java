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
@Import({UserOrganizationConverter.class, UserOrganizationRepositoryImpl.class})
class UserOrganizationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserOrganizationConverter converter;

    @Autowired
    private UserOrganizationRepositoryImpl repository;

    private UserOrganizationPO testUserOrg;
    private Long testSystemUserId;
    private String testOrgCode;

    @BeforeEach
    void setUp() {
        testSystemUserId = 1L;
        testOrgCode = "test-org-code";

        testUserOrg = new UserOrganizationPO();
        testUserOrg.setSystemUserId(testSystemUserId);
        testUserOrg.setOrgCode(testOrgCode);
        testUserOrg.setRole(UserRoleEnum.MEMBER.getCode().shortValue());
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
        assertEquals(testOrgCode, results.get(0).getOrgCode());
        assertEquals(UserRoleEnum.MEMBER, results.get(0).getRole());
    }

    @Test
    void findByOrgCode_Success() {
        List<UserOrganizationDO> results = repository.findByOrgCode(testOrgCode);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(testSystemUserId, results.get(0).getSystemUserId());
        assertEquals(testOrgCode, results.get(0).getOrgCode());
    }

    @Test
    void findBySystemUserIdAndOrgCode_Success() {
        Optional<UserOrganizationDO> result = repository.findBySystemUserIdAndOrgCode(testSystemUserId, testOrgCode);

        assertTrue(result.isPresent());
        assertEquals(testSystemUserId, result.get().getSystemUserId());
        assertEquals(testOrgCode, result.get().getOrgCode());
    }

    @Test
    void findBySystemUserIdAndOrgCode_NotFound() {
        Optional<UserOrganizationDO> result = repository.findBySystemUserIdAndOrgCode(999L, "non-existent");

        assertTrue(result.isEmpty());
    }

    @Test
    void save_Success() {
        UserOrganizationDO newUserOrg = new UserOrganizationDO();
        newUserOrg.setSystemUserId(2L);
        newUserOrg.setOrgCode("test-org-code-2");
        newUserOrg.setRole(UserRoleEnum.ADMIN);
        newUserOrg.setCreatedBy("test-user");
        newUserOrg.setUpdatedBy("test-user");

        UserOrganizationDO saved = repository.save(newUserOrg);

        assertNotNull(saved.getId());
        assertEquals(2L, saved.getSystemUserId());
        assertEquals("test-org-code-2", saved.getOrgCode());
        assertEquals(UserRoleEnum.ADMIN, saved.getRole());
    }

    @Test
    void deleteBySystemUserIdAndOrgCode_Success() {
        repository.deleteBySystemUserIdAndOrgCode(testSystemUserId, testOrgCode);

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
    void deleteByOrgCode_Success() {
        repository.deleteByOrgCode(testOrgCode);

        List<UserOrganizationDO> remaining = repository.findByOrgCode(testOrgCode);
        assertTrue(remaining.isEmpty());
    }

    @Test
    void existsBySystemUserIdAndOrgCode_True() {
        boolean exists = repository.existsBySystemUserIdAndOrgCode(testSystemUserId, testOrgCode);
        assertTrue(exists);
    }

    @Test
    void existsBySystemUserIdAndOrgCode_False() {
        boolean exists = repository.existsBySystemUserIdAndOrgCode(999L, "non-existent");
        assertFalse(exists);
    }
} 