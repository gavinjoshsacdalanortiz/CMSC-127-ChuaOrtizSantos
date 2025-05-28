package com.mcnz.spring.fee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.mcnz.spring.fee.FeeRepository.FeeProjection;
import com.mcnz.spring.fee.FeeRepository.FeeSummaryProjection;
import com.mcnz.spring.member.Member;
import com.mcnz.spring.membership.MemberOrganizationRoleRepository.AvailableAcademicYears;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fees")
public class FeeController {

    @Autowired
    private FeeRepository feeRepository;

    @GetMapping
    public ResponseEntity<List<Fee>> getAllFees() {
        List<Fee> fees = feeRepository.findAll();
        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @GetMapping("/{feeId}")
    public ResponseEntity<Fee> getFeeById(@PathVariable UUID feeId) {
        Optional<Fee> fee = feeRepository.findById(feeId);
        return fee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Fee>> getFeesByMemberId(@PathVariable UUID memberId) {
        List<Fee> fees = feeRepository.findByMemberId(memberId);
        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<FeeProjection>> getFeesByOrganizationId(@PathVariable UUID organizationId) {
        List<FeeProjection> fees = feeRepository.findByOrganizationId(organizationId);
        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}/me")
    public ResponseEntity<List<FeeProjection>> getFeesByOrganizationIdAndMemberId(@PathVariable UUID organizationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member currentMember = (Member) authentication.getPrincipal();
        List<FeeProjection> fees = feeRepository.findByOrganizationIdAndMemberId(organizationId,
                currentMember.getMemberId());
        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}/me/filter")
    public ResponseEntity<List<Fee>> filterFeesOfMember(
            @PathVariable UUID organizationId,
            @RequestParam(required = true) Integer semester,
            @RequestParam(required = true) Integer year) {

        List<Fee> fees;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member currentMember = (Member) authentication.getPrincipal();

        System.out.println(currentMember.getMemberId());

        if (semester != null && year != null) {
            fees = feeRepository.findBySemesterAndYearWithMemberId(semester, year, organizationId,
                    currentMember.getMemberId());
        } else {
            throw new Error("No fees");
        }

        return new ResponseEntity<>(fees, HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}/filter")
    public ResponseEntity<List<FeeProjection>> filterFees(
            @PathVariable UUID organizationId,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) Integer year) {

        List<FeeProjection> fees;

        fees = feeRepository.findByOrganizationIdWithFilter(organizationId, semester, year);

        return new ResponseEntity<>(fees, HttpStatus.OK);

    }

    @GetMapping("/available-years")
    @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<List<AvailableAcademicYears>> getAvailableAcademicYears(
            @PathVariable UUID organizationId

    ) {

        List<AvailableAcademicYears> years = feeRepository
                .getAvailableAcademicYears(organizationId);

        if (years.isEmpty()) {

        }

        return ResponseEntity.ok(years);
    }

    @PostMapping
    public ResponseEntity<String> createFee(@RequestBody Fee fee) {
        try {
            int result = feeRepository.save(
                    fee.getAmount(),
                    fee.getSemester(),
                    fee.getYear(),
                    fee.getDueDate(),
                    fee.getDatePaid(),
                    fee.getMemberId(),
                    fee.getOrganizationId());

            if (result > 0) {
                return new ResponseEntity<>("Fee created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to create fee", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{feeId}")
    public ResponseEntity<String> updateFee(@PathVariable UUID feeId, @RequestBody Fee fee) {
        try {
            int result = feeRepository.update(
                    feeId,
                    fee.getAmount(),
                    fee.getSemester(),
                    fee.getYear(),
                    fee.getDueDate(),
                    fee.getDatePaid(),
                    fee.getMemberId(),
                    fee.getOrganizationId());

            if (result > 0) {
                return new ResponseEntity<>("Fee updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Fee not found or no changes made", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{feeId}")
    public ResponseEntity<String> deleteFee(@PathVariable UUID feeId) {
        try {
            int result = feeRepository.delete(feeId);

            if (result > 0) {
                return new ResponseEntity<>("Fee deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Fee not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get members with unpaid fees for a given organization, semester, and year
    @GetMapping("/unpaid/members")
    public ResponseEntity<List<Map<String, Object>>> getMembersWithUnpaidFees(
            @RequestParam UUID organizationId,
            @RequestParam Integer semester,
            @RequestParam Integer year) {
        try {
            List<Object[]> results = feeRepository.findMembersWithUnpaidFees(organizationId, semester, year);
            List<Map<String, Object>> members = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> member = new HashMap<>();
                member.put("memberId", row[0]);
                member.put("firstName", row[1]);
                member.put("lastName", row[2]);
                member.put("email", row[3]);
                member.put("gender", row[4]);
                member.put("degreeProgram", row[5]);
                members.add(member);
            }

            return new ResponseEntity<>(members, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get a member's unpaid fees across all organizations
    @GetMapping("/unpaid/member/{memberId}")
    public ResponseEntity<List<Map<String, Object>>> getMemberUnpaidFees(@PathVariable UUID memberId) {
        try {
            List<Object[]> results = feeRepository.findUnpaidFeesByMember(memberId);
            List<Map<String, Object>> unpaidFees = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> feeInfo = new HashMap<>();
                feeInfo.put("feeId", row[0]);
                feeInfo.put("amount", row[1]);
                feeInfo.put("semester", row[2]);
                feeInfo.put("year", row[3]);
                feeInfo.put("dueDate", row[4]);
                feeInfo.put("organizationId", row[7]);
                feeInfo.put("organizationName", row[8]);
                unpaidFees.add(feeInfo);
            }

            return new ResponseEntity<>(unpaidFees, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get total paid and unpaid amounts for an organization as of a given date
    @GetMapping("/totals/{organizationId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<Map<String, Object>> getOrganizationFeeTotals(
            @PathVariable UUID organizationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        try {
            FeeSummaryProjection result = feeRepository.getTotalFeesAsOfDate(organizationId, asOfDate);

            Map<String, Object> totals = new HashMap<>();
            totals.put("totalPaid", result.getTotalPaid());
            totals.put("totalUnpaid", result.getTotalUnpaid());
            totals.put("organizationId", organizationId);
            totals.put("asOfDate", asOfDate);

            return new ResponseEntity<>(totals, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get members with highest debt for an organization in a given semester
    @GetMapping("/highest-debt")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<List<Map<String, Object>>> getMembersWithHighestDebt(
            @RequestParam UUID organizationId,
            @RequestParam Integer semester,
            @RequestParam Integer year) {
        try {
            List<Object[]> results = feeRepository.findMembersWithHighestDebt(organizationId, semester, year);
            List<Map<String, Object>> membersWithDebt = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> memberDebt = new HashMap<>();
                memberDebt.put("memberId", row[0]);
                memberDebt.put("firstName", row[1]);
                memberDebt.put("lastName", row[2]);
                memberDebt.put("email", row[3]);
                memberDebt.put("gender", row[4]);
                memberDebt.put("degreeProgram", row[5]);
                memberDebt.put("totalDebt", row[9]);
                membersWithDebt.add(memberDebt);
            }

            return new ResponseEntity<>(membersWithDebt, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get all late payments for an organization in a given semester and year
    @GetMapping("/late-payments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<List<Map<String, Object>>> getLatePayments(
            @RequestParam UUID organizationId,
            @RequestParam Integer semester,
            @RequestParam Integer year) {
        try {
            List<Object[]> results = feeRepository.findLatePayments(organizationId, semester, year);
            List<Map<String, Object>> latePayments = new ArrayList<>();

            for (Object[] row : results) {
                Map<String, Object> payment = new HashMap<>();
                payment.put("feeId", row[0]);
                payment.put("amount", row[1]);
                payment.put("semester", row[2]);
                payment.put("year", row[3]);
                payment.put("dueDate", row[4]);
                payment.put("datePaid", row[5]);
                payment.put("memberId", row[6]);
                payment.put("organizationId", row[7]);
                payment.put("memberFirstName", row[8]);
                payment.put("memberLastName", row[9]);
                payment.put("memberEmail", row[10]);
                latePayments.add(payment);
            }

            return new ResponseEntity<>(latePayments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
