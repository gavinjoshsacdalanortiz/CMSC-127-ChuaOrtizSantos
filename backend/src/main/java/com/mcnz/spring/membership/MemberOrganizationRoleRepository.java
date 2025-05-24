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
public interface MemberOrganizationRoleRepository
                extends JpaRepository<MemberOrganizationRole, UUID>, MemberOrganizationRoleRepositoryCustom {

        interface MemberRolesProjection {
                UUID getOrganizationId();

                String getRole();

                String getOrganizationName();
        }

        @Query(value = """
                        SELECT
                            mor.semester,
                            mor.year,
                            mor.organization_id AS organizationId,
                            r.name AS role,
                            o.organization_name AS organizationName
                        FROM member_organization_role AS mor
                        JOIN role r ON mor.role_id = r.role_id
                        JOIN organization o ON mor.organization_id = o.organization_id
                        WHERE
                            mor.member_id = :memberId
                            AND mor.year = date_part('year', NOW())
                            AND mor.semester = CASE
                                WHEN EXTRACT(MONTH FROM NOW()) BETWEEN 8 AND 1 THEN 1
                                WHEN EXTRACT(MONTH FROM NOW()) BETWEEN 1 AND 8 THEN 2
                                ELSE NULL
                            END
                        ORDER BY o.organization_id, (mor.year * 10 + mor.semester) DESC;
                                    """, nativeQuery = true)
        List<MemberRolesProjection> findLatestOrganizationRolesForMember(@Param("memberId") UUID memberId);

        @Query(value = """
                        SELECT
                            m.member_id, first_name, last_name, gender,
                            degree_program, email, batch, committee,
                            position, status
                        FROM member_organization_role mor
                        JOIN role r ON mor.role_id = r.role_id
                        JOIN member m ON mor.member_id = m.member_id
                        WHERE mor.organization_id = :organizationId
                          AND mor.member_id = :memberId
                          AND mor.year = :year
                          AND mor.semester = :semester
                        """, nativeQuery = true)
        Optional<MembershipDetails> findMembershipDetailsForSemester(
                        @Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId,
                        @Param("year") Integer year,
                        @Param("semester") Integer semester);

        @Query(value = """
                        SELECT m.member_id, m.first_name, m.last_name, m.gender,
                               m.degree_program, m.email, mor.batch,
                               mor.committee, mor.position, mor.status
                        FROM member_organization_role mor
                        JOIN member m ON mor.member_id = m.member_id
                        WHERE mor.organization_id = :organizationId
                          AND mor.member_id = :memberId
                        ORDER BY mor.year DESC, mor.semester DESC
                        LIMIT 1
                        """, nativeQuery = true)
        Optional<MembershipDetails> findLatestMembershipDetails(
                        @Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId);

        @Query(value = "SELECT EXISTS(SELECT 1 FROM member_organization_role " +
                        "WHERE organization_id = :organizationId AND member_id = :memberId)", nativeQuery = true)
        boolean existsByOrganizationIdAndMemberId(@Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId);

        @Modifying
        @Transactional
        @Query(value = """
                        DELETE FROM member_organization_role mor
                        WHERE mor.organization.id = :organizationId
                          AND mor.member.id = :memberId
                        """, nativeQuery = true)
        int removeAllMembershipsFromOrganization(
                        @Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId);

        @Modifying
        @Transactional
        @Query(value = """
                        UPDATE member_organization_role mor
                        SET committee = :committee,
                            position = :position,
                            status = :status
                        WHERE mor.organization.id = :organizationId
                          AND mor.member.id = :memberId
                          AND mor.year = :year
                          AND mor.semester = :semester
                        """, nativeQuery = true)
        int updateSemesterMembershipDetails(
                        @Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId,
                        @Param("committee") String committee,
                        @Param("position") String position,
                        @Param("status") String status,
                        @Param("year") Integer year,
                        @Param("semester") Integer semester);
}