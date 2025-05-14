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

}