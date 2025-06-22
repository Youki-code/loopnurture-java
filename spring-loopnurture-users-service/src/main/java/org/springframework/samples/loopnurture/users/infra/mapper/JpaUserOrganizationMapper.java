package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.users.infra.po.UserOrganizationPO;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserOrganizationMapper extends JpaRepository<UserOrganizationPO, Long> {
    List<UserOrganizationPO> findBySystemUserId(Long systemUserId);
    List<UserOrganizationPO> findByOrgCode(String orgCode);
    Optional<UserOrganizationPO> findBySystemUserIdAndOrgCode(Long systemUserId, String orgCode);
    void deleteBySystemUserIdAndOrgCode(Long systemUserId, String orgCode);
    void deleteBySystemUserId(Long systemUserId);
    void deleteByOrgCode(String orgCode);
    boolean existsBySystemUserIdAndOrgCode(Long systemUserId, String orgCode);

    @Modifying
    @Transactional
    @Query("update UserOrganizationPO u set u.deleted = true where u.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("update UserOrganizationPO u set u.deleted = true where u.systemUserId = :systemUserId")
    void softDeleteBySystemUserId(@Param("systemUserId") Long systemUserId);

    @Modifying
    @Transactional
    @Query("update UserOrganizationPO u set u.deleted = true where u.orgCode = :orgCode")
    void softDeleteByOrgCode(@Param("orgCode") String orgCode);

    @Modifying
    @Transactional
    @Query("update UserOrganizationPO u set u.deleted = true where u.systemUserId = :systemUserId and u.orgCode = :orgCode")
    void softDeleteBySystemUserIdAndOrgCode(@Param("systemUserId") Long systemUserId, @Param("orgCode") String orgCode);
} 