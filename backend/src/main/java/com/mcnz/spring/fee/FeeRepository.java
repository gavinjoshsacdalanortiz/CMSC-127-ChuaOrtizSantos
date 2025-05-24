package com.mcnz.spring.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeeRepository extends JpaRepository<Fee, UUID> {

        @Query(value = "SELECT * FROM fees", nativeQuery = true)
        List<Fee> findAll();

        @Query(value = "SELECT * FROM fees WHERE fee_id = :feeId", nativeQuery = true)
        Optional<Fee> findById(@Param("feeId") UUID feeId);

        @Query(value = "SELECT * FROM fees WHERE member_id = :memberId", nativeQuery = true)
        List<Fee> findByMemberId(@Param("memberId") UUID memberId);

        @Query(value = "SELECT * FROM fees WHERE semester = :semester", nativeQuery = true)
        List<Fee> findBySemester(@Param("semester") Integer semester);

        @Query(value = "SELECT * FROM fees WHERE year = :year", nativeQuery = true)
        List<Fee> findByYear(@Param("year") Integer year);

        @Query(value = "SELECT * FROM fees WHERE semester = :semester AND year = :year", nativeQuery = true)
        List<Fee> findBySemesterAndYear(@Param("semester") Integer semester,
                        @Param("year") Integer year);

        @Query(value = "SELECT * FROM fees WHERE id = :organizationId", nativeQuery = true)
        List<Fee> findByOrganizationId(@Param("organizationId") UUID organizationId);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO fees (amount, semester, year, due_date, date_paid, member_id, id) " +
                        "VALUES (:amount, :semester, :year, :dueDate, :datePaid, :memberId, :organizationId)", nativeQuery = true)
        int save(@Param("amount") BigDecimal amount, @Param("semester") Integer semester,
                        @Param("year") Integer year, @Param("dueDate") LocalDate dueDate,
                        @Param("datePaid") LocalDate datePaid, @Param("memberId") UUID memberId,
                        @Param("organizationId") UUID organizationId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE fees SET amount = :amount, semester = :semester, year = :year, " +
                        "due_date = :dueDate, date_paid = :datePaid, member_id = :memberId, id = :organizationId " +
                        "WHERE fee_id = :feeId", nativeQuery = true)
        int update(@Param("feeId") UUID feeId, @Param("amount") BigDecimal amount,
                        @Param("semester") Integer semester, @Param("year") Integer year,
                        @Param("dueDate") LocalDate dueDate, @Param("datePaid") LocalDate datePaid,
                        @Param("memberId") UUID memberId, @Param("organizationId") UUID organizationId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM fees WHERE fee_id = :feeId", nativeQuery = true)
        int delete(@Param("feeId") UUID feeId);

        @Query(value = "SELECT COUNT(*) FROM fees", nativeQuery = true)
        long count();
}