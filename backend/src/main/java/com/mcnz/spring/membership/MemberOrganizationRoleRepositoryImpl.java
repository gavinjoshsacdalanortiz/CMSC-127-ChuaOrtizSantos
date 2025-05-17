package com.mcnz.spring.membership;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import com.mcnz.spring.membership.payload.MembershipDetails;

import java.util.List;
import java.util.UUID;

@Repository
public class MemberOrganizationRoleRepositoryImpl {

    private final EntityManager entityManager;

    public MemberOrganizationRoleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<MembershipDetails> findMembersByOrganizationIdWithFilters(UUID organizationId, String role,
            String status, String gender, String degreeProgram, Integer year) {
        StringBuilder sql = new StringBuilder(
                "SELECT m.member_id, first_name, last_name, gender, degree_program, email, batch, committee, position, status "
                        +
                        "FROM member_organization_role mor " +
                        "JOIN role r ON mor.role_id = r.role_id " +
                        "JOIN member m ON mor.member_id = m.member_id " +
                        "WHERE mor.organization_id = :organizationId");

        // Dynamic filters
        if (role != null)
            sql.append(" AND mor.position = :role");
        if (status != null)
            sql.append(" AND mor.status = :status");
        if (gender != null)
            sql.append(" AND m.gender = :gender");
        if (degreeProgram != null)
            sql.append(" AND m.degree_program = :degreeProgram");
        if (year != null)
            sql.append(" AND mor.batch = :year");

        Query query = entityManager.createNativeQuery(sql.toString(), "MembershipDetailsMapping");
        query.setParameter("organizationId", organizationId);

        if (role != null)
            query.setParameter("role", role);
        if (status != null)
            query.setParameter("status", status);
        if (gender != null)
            query.setParameter("gender", gender);
        if (degreeProgram != null)
            query.setParameter("degreeProgram", degreeProgram);
        if (year != null)
            query.setParameter("year", year.toString());

        return query.getResultList();
    }
}
