package com.mcnz.spring.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberOrganizationRoleRepository extends JpaRepository<MemberOrganizationRole, UUID> {
        interface MembershipDetailsProjection {
                UUID getOrganizationId();

                String getRole();

                String getPosition();
        }

        @Query(value = "SELECT " +
                        "mor.organization_id AS organizationId, " +
                        "r.name AS role, " +
                        "mor.position AS position " +
                        "FROM member_organization_role AS mor " +
                        "JOIN role r ON mor.role_id = r.role_id " +
                        "WHERE mor.member_id = :memberId", nativeQuery = true)
        List<MembershipDetailsProjection> findMembershipDetailsByUserId(@Param("memberId") UUID memberId);

        @Query(value = "SELECT * " +
                        "FROM member_organization_role mor " +
                        "WHERE mor.organization_id = :organizationId", nativeQuery = true)
        List<MemberOrganizationRole> findByOrganizationId(@Param("organizationId") UUID organizationId);

        @Query(value = "SELECT * " +
                        "FROM member_organization_role mor " +
                        "WHERE mor.member_id = :memberId AND mor.organization_id = :organizationId", nativeQuery = true)
        Optional<MemberOrganizationRole> findByMemberIdAndOrganizationId(@Param("memberId") UUID memberId,
                        @Param("organizationId") UUID organizationId);
}