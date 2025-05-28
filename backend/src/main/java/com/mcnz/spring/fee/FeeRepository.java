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

        @Query(value = "SELECT * FROM fee", nativeQuery = true)
        List<Fee> findAll();

        @Query(value = "SELECT * FROM fee WHERE fee_id = :feeId", nativeQuery = true)
        Optional<Fee> findById(@Param("feeId") UUID feeId);

        @Query(value = "SELECT * FROM fee WHERE member_id = :memberId", nativeQuery = true)
        List<Fee> findByMemberId(@Param("memberId") UUID memberId);

        @Query(value = "SELECT * FROM fee WHERE semester = :semester AND organization_id = :organizationId", nativeQuery = true)
        List<Fee> findBySemester(@Param("semester") Integer semester,
                        @Param("organizationId") UUID organizationId);

        @Query(value = "SELECT * FROM fee WHERE year = :year AND organization_id = :organizationId", nativeQuery = true)
        List<Fee> findByYear(@Param("year") Integer year,
                        @Param("organizationId") UUID organizationId);

        @Query(value = "SELECT * FROM fee WHERE semester = :semester AND year = :year AND organization_id = :organizationId", nativeQuery = true)
        List<Fee> findBySemesterAndYear(@Param("semester") Integer semester,
                        @Param("year") Integer year,
                        @Param("organizationId") UUID organizationId);

        @Query(value = "SELECT * FROM fee WHERE semester = :semester AND year = :year AND organization_id = :organizationId AND member_id = :memberId", nativeQuery = true)
        List<Fee> findBySemesterAndYearWithMemberId(@Param("semester") Integer semester,
                        @Param("year") Integer year,
                        @Param("organizationId") UUID organizationId, @Param("memberId") UUID memberId);

        @Query(value = "SELECT * FROM fee WHERE organization_id = :organizationId", nativeQuery = true)
        List<Fee> findByOrganizationId(@Param("organizationId") UUID organizationId);

        @Query(value = "SELECT * FROM fee WHERE organization_id = :organizationId AND member_id = :memberId", nativeQuery = true)
        List<Fee> findByOrganizationIdAndMemberId(@Param("organizationId") UUID organizationId,
                        @Param("memberId") UUID memberId);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO fee (amount, semester, year, due_date, date_paid, member_id, id) " +
                        "VALUES (:amount, :semester, :year, :dueDate, :datePaid, :memberId, :organizationId)", nativeQuery = true)
        int save(@Param("amount") BigDecimal amount, @Param("semester") Integer semester,
                        @Param("year") Integer year, @Param("dueDate") LocalDate dueDate,
                        @Param("datePaid") LocalDate datePaid, @Param("memberId") UUID memberId,
                        @Param("organizationId") UUID organizationId);

        @Modifying
        @Transactional
        @Query(value = "UPDATE fee SET amount = :amount, semester = :semester, year = :year, " +
                        "due_date = :dueDate, date_paid = :datePaid, member_id = :memberId, id = :organizationId "
                        +
                        "WHERE fee_id = :feeId", nativeQuery = true)
        int update(@Param("feeId") UUID feeId, @Param("amount") BigDecimal amount,
                        @Param("semester") Integer semester, @Param("year") Integer year,
                        @Param("dueDate") LocalDate dueDate,
                        @Param("datePaid") LocalDate datePaid,
                        @Param("memberId") UUID memberId,
                        @Param("organizationId") UUID organizationId);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM fee WHERE fee_id = :feeId", nativeQuery = true)
        int delete(@Param("feeId") UUID feeId);

        @Query(value = "SELECT COUNT(*) FROM fee", nativeQuery = true)
        long count();

        // find members with unpaid fee for a given organization, semester, and year
        @Query(value = """
                        SELECT DISTINCT m.* FROM member m
                        JOIN fee f ON m.member_id = f.member_id
                        WHERE f.organization_id = :organizationId
                        AND f.semester = :semester
                        AND f.year = :year
                        AND f.date_paid IS NULL
                        """, nativeQuery = true)
        List<Object[]> findMembersWithUnpaidFees(@Param("organizationId") UUID organizationId,
                        @Param("semester") Integer semester,
                        @Param("year") Integer year);

        // find unpaid fee for a specific member across all organizations
        @Query(value = """
                        SELECT f.*, o.organization_name FROM fee f
                        LEFT JOIN organization o ON f.organization_id = o.organization_id
                        WHERE f.member_id = :memberId
                        AND f.date_paid IS NULL
                        """, nativeQuery = true)
        List<Object[]> findUnpaidFeesByMember(@Param("memberId") UUID memberId);

        public interface FeeSummaryProjection {
                java.math.BigDecimal getTotalPaid(); // Assuming amount is BigDecimal

                java.math.BigDecimal getTotalUnpaid();
        }

        // get total paid and unpaid amounts for an organization as of a given date
        @Query(value = """
                        SELECT
                        COALESCE(SUM(CASE WHEN f.date_paid IS NOT NULL AND f.date_paid <= :asOfDate THEN f.amount ELSE 0 END), 0) as total_paid,
                        COALESCE(SUM(CASE WHEN f.date_paid IS NULL OR f.date_paid > :asOfDate THEN f.amount ELSE 0 END), 0) as total_unpaid
                        FROM fee f
                        WHERE f.organization_id = :organizationId
                        AND f.due_date <= :asOfDate
                        """, nativeQuery = true)
        FeeSummaryProjection getTotalFeesAsOfDate(@Param("organizationId") UUID organizationId,
                        @Param("asOfDate") LocalDate asOfDate);

        // find members with highest debt for an organization in a given semester
        @Query(value = """
                        SELECT m.*, SUM(f.amount) as total_debt
                        FROM member m
                        JOIN fee f ON m.member_id = f.member_id
                        WHERE f.organization_id = :organizationId
                        AND f.semester = :semester
                        AND f.year = :year
                        AND f.date_paid IS NULL
                        GROUP BY m.member_id, m.first_name, m.last_name, m.email, m.gender, m.degree_program, m.created_at, m.updated_at
                        ORDER BY total_debt DESC
                        """, nativeQuery = true)
        List<Object[]> findMembersWithHighestDebt(@Param("organizationId") UUID organizationId,
                        @Param("semester") Integer semester,
                        @Param("year") Integer year);

        // find all late payments for an organization in a given semester and year
        @Query(value = """
                        SELECT f.*, m.first_name, m.last_name, m.email
                        FROM fee f
                        JOIN member m ON f.member_id = m.member_id
                        WHERE f.organization_id = :organizationId
                        AND f.semester = :semester
                        AND f.year = :year
                        AND f.date_paid IS NOT NULL
                        AND f.date_paid > f.due_date
                        ORDER BY f.date_paid DESC
                        """, nativeQuery = true)
        List<Object[]> findLatePayments(@Param("organizationId") UUID organizationId,
                        @Param("semester") Integer semester,
                        @Param("year") Integer year);
}
