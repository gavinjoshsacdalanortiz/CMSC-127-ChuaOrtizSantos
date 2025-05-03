package com.mcnz.spring.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserOrganizationRoleRepository extends JpaRepository<UserOrganizationRole, UUID> {

        @Query(value = "SELECT * FROM user_organization_role WHERE user_id = :userId", nativeQuery = true)
        List<UserOrganizationRole> findByUser_Id(@Param("userId") UUID userId);
}