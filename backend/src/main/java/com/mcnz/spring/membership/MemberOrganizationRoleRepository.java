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

                String getOrganizationName();
        }

        @Query(value = "SELECT " +
                        "mor.organization_id AS organizationId, " +
                        "r.name AS role, " +
                        "o.organization_name " +
                        "FROM member_organization_role AS mor " +
                        "JOIN role r ON mor.role_id = r.role_id " +
                        "JOIN organization o ON mor.organization_id = o.organization_id " +
                        "WHERE mor.member_id = :memberId", nativeQuery = true)
        List<MemberRolesProjection> findRolesByMemberId(@Param("memberId") UUID memberId);

        @Query(value = "SELECT " +
                        "    m.member_id AS memberId, " +
                        "    m.first_name AS firstName, " +
                        "    m.last_name AS lastName, " +
                        "    m.gender AS gender, " +
                        "    m.degree_program AS degreeProgram, " +
                        "    m.email AS email, " +
                        "    m.graduation_year AS batch, " + // Assuming 'batch' in your projection maps to
                                                             // 'graduation_year' in member table
                        "    mor.committee AS committee, " +
                        "    mor.position AS position, " +
                        "    mor.status AS status, " +
                        "    r.name AS securityRoleName " + // Select the role name for filtering
                        "FROM " +
                        "    member_organization_role mor " +
                        "JOIN " +
                        "    member m ON mor.member_id = m.member_id " +
                        "JOIN " +
                        "    role r ON mor.role_id = r.role_id " + // Ensure role PK is role_id
                        "WHERE " +
                        "    mor.organization_id = :organizationId " +
                        "    AND (:position IS NULL OR mor.position = :position) " +
                        "    AND (:status IS NULL OR mor.status = :status) " +
                        "    AND (:gender IS NULL OR m.gender = :gender) " +
                        "    AND (:degreeProgram IS NULL OR m.degree_program = :degreeProgram) " +
                        "    AND (:year IS NULL OR m.graduation_year = CAST(:year AS VARCHAR)) ", // Cast year to
                                                                                                  // VARCHAR to match
                                                                                                  // graduation_year if
                                                                                                  // it's VARCHAR
                        nativeQuery = true)
        List<MembershipDetails> findMembersByOrganizationIdAndFilters(
                        @Param("organizationId") UUID organizationId,
                        @Param("position") String position, // Changed from 'role' to match
                        @Param("committee") String committe, // projection/db
                        @Param("status") String status,
                        @Param("gender") String gender,
                        @Param("degreeProgram") String degreeProgram,
                        @Param("year") Integer year // Assuming graduation_year in DB might be VARCHAR, hence the CAST
        );

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

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM member_organization_role mor "
                        +
                        "WHERE mor.organization_id = :organizationId AND mor.member_id = :memberId", nativeQuery = true)
        int deleteByMemberIdAndOrganizationId(@Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE member_organization_role mor SET committee= :committee, position= :position, status= :status "
                        +
                        "WHERE mor.organization_id = :organizationId AND mor.member_id = :memberId", nativeQuery = true)
        int updateByMemberIdAndOrganizationId(@Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId, @Param("committee") String committee,
                        @Param("position") String position, @Param("status") String status);
}
