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
            Integer batch,
            Integer batchStart,
            Integer batchEnd,
            Integer year,
            Integer yearStart,
            Integer yearEnd,
            Integer semester,
            Integer semesterStart,
            Integer semesterEnd,
            String sortBy,
            String sortDirection) {

        boolean filteringByYearSemester = (year != null && semester != null) ||
                yearStart != null || yearEnd != null ||
                semesterStart != null || semesterEnd != null;

        boolean usingCustomSort = sortBy != null && !sortBy.isBlank();
        boolean useDistinctLatestOnly = !filteringByYearSemester && !usingCustomSort;

        StringBuilder sql = new StringBuilder();

        if (useDistinctLatestOnly) {
            sql.append("SELECT DISTINCT ON (m.member_id) ");
        } else {
            sql.append("SELECT ");
        }

        sql.append("m.member_id, first_name, last_name, gender, degree_program, email,  ")
                .append("mor.batch, mor.year, mor.semester, mor.committee, mor.position, mor.status ")
                .append("FROM member_organization_role mor ")
                .append("JOIN role r ON mor.role_id = r.role_id ")
                .append("JOIN member m ON mor.member_id = m.member_id ")
                .append("WHERE mor.organization_id = :organizationId");

        if (position != null)
            sql.append(" AND mor.position = :position");
        if (committee != null)
            sql.append(" AND mor.committee = :committee");
        if (status != null)
            sql.append(" AND mor.status = CAST(:status AS member_status_enum)");
        if (gender != null)
            sql.append(" AND m.gender = :gender");
        if (degreeProgram != null)
            sql.append(" AND m.degree_program = :degreeProgram");

        if (batch != null) {
            sql.append(" AND mor.batch = :batch");
        } else {
            if (batchStart != null)
                sql.append(" AND mor.batch >= :batchStart");
            if (batchEnd != null)
                sql.append(" AND mor.batch <= :batchEnd");
        }

        if (year != null && semester != null) {
            sql.append(" AND mor.year = :year AND mor.semester = :semester");
        } else if (year != null) {
            sql.append(" AND mor.year = :year");
        } else if (yearStart != null && semesterStart != null && yearEnd != null && semesterEnd != null) {
            sql.append(
                    " AND (mor.year * 10 + mor.semester) BETWEEN (:yearStart * 10 + :semesterStart) AND (:yearEnd * 10 + :semesterEnd)");
        } else if (yearStart != null && semesterStart != null) {
            sql.append(" AND (mor.year * 10 + mor.semester) >= (:yearStart * 10 + :semesterStart)");
        } else if (yearEnd != null && semesterEnd != null) {
            sql.append(" AND (mor.year * 10 + mor.semester) <= (:yearEnd * 10 + :semesterEnd)");
        } else if (yearStart != null) {
            sql.append(" AND mor.year >= :yearStart");
        } else if (yearEnd != null) {
            sql.append(" AND mor.year <= :yearEnd");
        }

        if (useDistinctLatestOnly) {
            sql.append(" ORDER BY m.member_id, (mor.year * 10 + mor.semester) DESC");
        } else if (usingCustomSort) {
            List<String> allowedSortFields = List.of("position", "status", "committee", "batch", "semester",
                    "first_name", "last_name", "gender", "degree_program", "year");
            List<String> allowedDirections = List.of("asc", "desc");

            if (allowedSortFields.contains(sortBy.toLowerCase())) {
                String direction = (sortDirection != null && allowedDirections.contains(sortDirection.toLowerCase()))
                        ? sortDirection
                        : "asc";
                sql.append(" ORDER BY ").append(sortBy).append(" ").append(direction.toUpperCase());
            }
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
        if (batch != null)
            query.setParameter("batch", batch);
        if (batchStart != null)
            query.setParameter("batchStart", batchStart);
        if (batchEnd != null)
            query.setParameter("batchEnd", batchEnd);
        if (year != null && semester != null) {
            query.setParameter("year", year);
            query.setParameter("semester", semester);
        }
        if (year != null) {
            query.setParameter("year", year);
        }
        if (yearStart != null)
            query.setParameter("yearStart", yearStart);
        if (semesterStart != null)
            query.setParameter("semesterStart", semesterStart);
        if (yearEnd != null)
            query.setParameter("yearEnd", yearEnd);
        if (semesterEnd != null)
            query.setParameter("semesterEnd", semesterEnd);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new MembershipDetails(
                        (UUID) row[0], // Member Id
                        (String) row[1], // First name
                        (String) row[2], // Last name
                        (String) row[3], // Gender
                        (String) row[4], // Degree Program
                        (String) row[5], // email
                        (Integer) row[6], // batch
                        (Integer) row[7], // year
                        (Integer) row[8], // semester
                        (String) row[9], // committee
                        (String) row[10], // position
                        (String) row[11])) // status
                .toList();
    }
}
