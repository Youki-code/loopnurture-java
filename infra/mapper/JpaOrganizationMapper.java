package org.springframework.samples.loopnurture.users.infra.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import org.springframework.stereotype.Repository;
 
@Repository
public interface JpaOrganizationMapper extends JpaRepository<OrganizationPO, String> {
    boolean existsByOrgCode(String orgCode);
    OrganizationPO findByOrgCode(String orgCode);
} 