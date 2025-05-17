package com.mcnz.spring.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

        @Query(value = "SELECT * FROM member", nativeQuery = true)
        List<Member> findAll();

        @Query(value = "SELECT * FROM member WHERE id = :id", nativeQuery = true)
        Optional<Member> findById(@Param("id") UUID id);

        @Query(value = "SELECT * FROM member WHERE email = :email", nativeQuery = true)
        Optional<Member> findByEmail(@Param("email") String email);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO member (id, first_name, last_name, gender, degree_program, batch, email, password, created_at, updated_at) "
                        +
                        "VALUES (:id, :firstName, :lastName, :gender, :degreeProgram, :batch, :email, :password, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)", nativeQuery = true)
        int save(@Param("id") UUID id, @Param("firstName") String firstName, @Param("lastName") String lastName,
                        @Param("gender") String gender, @Param("degreeProgram") String degreeProgram,
                        @Param("batch") String batch,
                        @Param("email") String email, @Param("password") String password);

        @Modifying
        @Transactional
        @Query(value = "UPDATE member SET first_name = :firstName, last_name =:lastName, gender = :gender, " +
                        "degree_program = :degreeProgram, email = :email, " +
                        "updated_at = CURRENT_TIMESTAMP " +
                        "WHERE id = :id", nativeQuery = true)
        int update(@Param("id") UUID id, @Param("firstName") String firstName,
                        @Param("lastName") String lastName,
                        @Param("gender") String gender, @Param("degreeProgram") String degreeProgram,
                        @Param("email") String email);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM member WHERE id = :id", nativeQuery = true)
        int delete(@Param("id") UUID id);

        @Query(value = "SELECT COUNT(*) FROM member", nativeQuery = true)
        long count();

        @Query(value = "SELECT * FROM member m JOIN member_organization mo ON m.id = mo.member_id " +
        "WHERE mo.organization_id = :organizationId", nativeQuery = true)
        List<Member> findByOrganizationId(@Param("organizationId") Long organizationId);

        @Query(value = "SELECT * FROM member m JOIN member_organization mo ON m.id = mo.member_id " +
        "WHERE mo.organization_id = :organizationId AND mo.role = :role", nativeQuery = true)
        List<Member> findByOrganizationIdAndRole(@Param("organizationId") Long organizationId, @Param("role") String role);

        @Query(value = "SELECT * FROM member m JOIN member_organization mo ON m.id = mo.member_id " +
        "WHERE mo.organization_id = :organizationId AND mo.status = :status", nativeQuery = true)
        List<Member> findByOrganizationIdAndStatus(@Param("organizationId") Long organizationId, @Param("status") String status);

        @Query(value = "SELECT * FROM member WHERE gender = :gender", nativeQuery = true)
        List<Member> findByGender(@Param("gender") String gender);

        @Query(value = "SELECT * FROM member WHERE degree_program = :degreeProgram", nativeQuery = true)
        List<Member> findByDegreeProgram(@Param("degreeProgram") String degreeProgram);

        @Query(value = "SELECT * FROM member WHERE EXTRACT(YEAR FROM created_at) = :year", nativeQuery = true)
        List<Member> findByYear(@Param("year") int year);

        @Query(value = "SELECT * FROM member m JOIN member_organization mo ON m.id = mo.member_id " +
        "WHERE mo.organization_id = :organizationId " +
        "AND (:role IS NULL OR mo.role = :role) " +
        "AND (:status IS NULL OR mo.status = :status) " +
        "AND (:gender IS NULL OR m.gender = :gender) " +
        "AND (:degreeProgram IS NULL OR m.degree_program = :degreeProgram) " +
        "AND (:year IS NULL OR EXTRACT(YEAR FROM m.created_at) = :year)", nativeQuery = true)
        List<Member> filterMembers(
        @Param("organizationId") Long organizationId,
        @Param("role") String role,
        @Param("status") String status,
        @Param("gender") String gender,
        @Param("degreeProgram") String degreeProgram,
        @Param("year") Integer year);
}