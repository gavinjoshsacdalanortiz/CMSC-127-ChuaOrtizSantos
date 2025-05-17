package com.mcnz.spring.membership;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import com.mcnz.spring.membership.payload.MembershipDetails;

import java.util.List;
import java.util.UUID;

@Repository
public class MemberOrganizationRoleRepositoryImpl implements MemberOrganizationRoleRepositoryCustom {

    private final EntityManager entityManager;

    public MemberOrganizationRoleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<MembershipDetails> findMembersByOrganizationIdWithFilters(
            UUID organizationId,
            String position,
            String committee,
            String status,
            String gender,
            String degreeProgram,
            Integer year,
            String sortBy,
            String sortDirection) {

        StringBuilder sql = new StringBuilder(
                "SELECT m.member_id, first_name, last_name, gender, degree_program, email, batch, committee, position, status "
                        +
                        "FROM member_organization_role mor " +
                        "JOIN role r ON mor.role_id = r.role_id " +
                        "JOIN member m ON mor.member_id = m.member_id " +
                        "WHERE mor.organization_id = :organizationId");

        if (position != null)
            sql.append(" AND mor.position = :position");
        if (committee != null)
            sql.append(" AND mor.committee = :committee");
        if (status != null)
            sql.append(" AND mor.status = :status");
        if (gender != null)
            sql.append(" AND m.gender = :gender");
        if (degreeProgram != null)
            sql.append(" AND m.degree_program = :degreeProgram");
        if (year != null)
            sql.append(" AND mor.batch = :year");

        // Allowed columns for sorting to prevent SQL injection
        List<String> allowedSortFields = List.of(
                "position", "status", "committee", "batch", "firstName", "lastName", "gender", "degreeProgram");
        List<String> allowedDirections = List.of("asc", "desc");

        if (sortBy != null && allowedSortFields.contains(sortBy.toLowerCase())) {
            String direction = (sortDirection != null && allowedDirections.contains(sortDirection.toLowerCase()))
                    ? sortDirection
                    : "asc"; // default to ASC

            sql.append(" ORDER BY ").append(sortBy).append(" ").append(direction.toUpperCase());
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("organizationId", organizationId);

        if (position != null)
            query.setParameter("position", position);
        if (committee != null)
            query.setParameter("committee", committee);
        if (status != null)
            query.setParameter("status", status);
        if (gender != null)
            query.setParameter("gender", gender);
        if (degreeProgram != null)
            query.setParameter("degreeProgram", degreeProgram);
        if (year != null)
            query.setParameter("year", year.toString());

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new MembershipDetails(
                        (UUID) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        (String) row[5],
                        (String) row[6],
                        (String) row[7],
                        (String) row[8],
                        (String) row[9]))
                .toList();
    }

}
