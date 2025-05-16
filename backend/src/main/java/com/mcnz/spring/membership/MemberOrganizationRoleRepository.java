package com.mcnz.spring.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mcnz.spring.membership.payload.MembershipDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberOrganizationRoleRepository extends JpaRepository<MemberOrganizationRole, UUID> {
        interface MemberRolesProjection {
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
        List<MemberRolesProjection> findRolesByMemberId(@Param("memberId") UUID memberId);

        @Query(value = "SELECT m.member_id, first_name, last_name, gender, degree_program, email, batch, committee, position, status "
                        +
                        "FROM member_organization_role mor JOIN role r ON mor.role_id = r.role_id JOIN member m ON mor.member_id = m.member_id "
                        +
                        "WHERE mor.organization_id = :organizationId", nativeQuery = true)
        List<MembershipDetails> findMembersByOrganizationId(@Param("organizationId") UUID organizationId);

        @Query(value = "SELECT m.member_id, first_name, last_name, gender, degree_program, email, batch, committee, position, status "
                        +
                        "FROM member_organization_role mor JOIN role r ON mor.role_id = r.role_id JOIN member m ON mor.member_id = m.member_id "
                        +
                        "WHERE mor.organization_id = :organizationId AND mor.member_id = :memberId", nativeQuery = true)
        Optional<MembershipDetails> findByMemberIdAndOrganizationId(@Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId);

        // @Query(value = "DELETE FROM member_organization_role"
        // +
        // "WHERE mor.organization_id = :organizationId AND mor.member_id = :memberId",
        // nativeQuery = true)
        // int updateByMemberIdAndOrganizationId(@Param("organizationId") UUID
        // organizationId,
        // @Param("memberId") UUID memberId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM member_organization_role mor "
                        +
                        "WHERE mor.organization_id = :organizationId AND mor.member_id = :memberId", nativeQuery = true)
        int deleteByMemberIdAndOrganizationId(@Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId);
}
