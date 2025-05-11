package com.mcnz.spring.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserOrganizationRoleRepository extends JpaRepository<UserOrganizationRole, UUID> {
        interface MembershipDetailsProjection {
                UUID getOrganizationId();

                String getRole();

                String getPosition();
        }

        @Query(value = "SELECT " +
                        "uor.organization_id AS organizationId, " +
                        "r.name AS role, " +
                        "uor.position AS position " +
                        "FROM user_organization_role AS uor " +
                        "JOIN roles r ON uor.role_id = r.id " +
                        "WHERE uor.user_id = :userId", nativeQuery = true)
        List<MembershipDetailsProjection> findMembershipDetailsByUserId(@Param("userId") UUID userId);
}