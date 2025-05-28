package com.mcnz.spring.membership;

import com.mcnz.spring.member.Member;
import com.mcnz.spring.member.MemberRepository;
import com.mcnz.spring.membership.MemberOrganizationRoleRepository.AvailableAcademicYears;
import com.mcnz.spring.membership.MemberOrganizationRoleRepository.MembershipStatusPercentage;
import com.mcnz.spring.membership.payload.MembershipDetails;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.mcnz.spring.status.Status;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/membership/{organizationId}/members")
public class MemberOrganizationRoleController {

    private final MemberOrganizationRoleRepository memberOrganizationRoleRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public MemberOrganizationRoleController(
            MemberOrganizationRoleRepository memberOrganizationRoleRepository,
            MemberRepository memberRepository) {
        this.memberOrganizationRoleRepository = memberOrganizationRoleRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<List<MembershipDetails>> getOrganizationMembers(
            @PathVariable UUID organizationId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String committee,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String degreeProgram,
            @RequestParam(required = false) Integer batch,
            @RequestParam(required = false) Integer batchStart,
            @RequestParam(required = false) Integer batchEnd,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer yearStart,
            @RequestParam(required = false) Integer yearEnd,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) Integer semesterStart,
            @RequestParam(required = false) Integer semesterEnd,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {

        List<MembershipDetails> members = memberOrganizationRoleRepository
                .findMembersByOrganizationIdWithFilters(
                        organizationId,
                        position,
                        committee,
                        status,
                        gender,
                        degreeProgram,
                        batch,
                        batchStart,
                        batchEnd,
                        year,
                        yearStart,
                        yearEnd,
                        semester,
                        semesterStart,
                        semesterEnd,
                        sortBy,
                        sortDirection);

        return ResponseEntity.ok(members);
    }

    // @GetMapping("/committees/available")
    // public ResponseEntity<List<String>> getAllCommittees(@PathVariable UUID
    // organizationId) {
    // List<String> committees =
    // memberOrganizationRoleRepository.getAvailableCommittees(
    // organizationId);
    // return new ResponseEntity<>(committees, HttpStatus.OK);
    // }

    @GetMapping("/{memberId}")
    @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<MembershipDetails> getMemberMembership(
            @PathVariable UUID organizationId,
            @PathVariable UUID memberId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {

        Optional<MembershipDetails> membership;

        if (year != null && semester != null) {
            membership = memberOrganizationRoleRepository
                    .findMembershipDetailsForSemester(
                            organizationId,
                            memberId,
                            year,
                            semester);
        } else {
            // Get latest membership
            // if no year/semester
            // specified
            membership = memberOrganizationRoleRepository
                    .findLatestMembershipDetails(organizationId,
                            memberId);
        }

        return membership.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member with ID " + memberId + " is not part of organization "
                                + organizationId));
    }

    @GetMapping("/status-percentages")
    @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<List<MembershipStatusPercentage>> getMembershipStatusPercentages(
            @PathVariable UUID organizationId,
            @RequestParam Integer startYear, // Parameter
                                             // from
                                             // frontend
            @RequestParam Integer startSemester // Parameter
                                                // from
                                                // frontend
    ) {

        List<MembershipStatusPercentage> percentages = memberOrganizationRoleRepository
                .getMembershipStatusPercentages(organizationId,
                        startYear,
                        startSemester);

        if (percentages.isEmpty()) {
            // You can choose to
            // return an empty list
            // or a 404 if no data
            // for that
            // period.
            // For simplicity, an
            // empty list is often
            // fine.
            // Or: throw new
            // ResponseStatusException(HttpStatus.NOT_FOUND,
            // "No
            // membership data found
            // for the specified
            // period.");
        }

        return ResponseEntity.ok(percentages);
    }

    @GetMapping("/available-years")
    @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<List<AvailableAcademicYears>> getAvailableAcademicYears(
            @PathVariable UUID organizationId

    ) {

        List<AvailableAcademicYears> years = memberOrganizationRoleRepository
                .getAvailableAcademicYears(organizationId);

        if (years.isEmpty()) {
            // You can choose to
            // return an empty list
            // or a 404 if no data
            // for that
            // period.
            // For simplicity, an
            // empty list is often
            // fine.
            // Or: throw new
            // ResponseStatusException(HttpStatus.NOT_FOUND,
            // "No
            // membership data found
            // for the specified
            // period.");
        }

        return ResponseEntity.ok(years);
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<Void> removeMemberFromOrganization(
            @PathVariable UUID organizationId,
            @PathVariable UUID memberId) {

        if (!memberOrganizationRoleRepository.existsByOrganizationIdAndMemberId(
                organizationId,
                memberId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Member with ID " + memberId + " is not part of organization "
                            + organizationId);
        }

        memberOrganizationRoleRepository.removeAllMembershipsFromOrganization(
                organizationId,
                memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<MembershipDetails> addMemberToOrganization(
            @PathVariable UUID organizationId,
            @Valid @RequestBody MembershipDetails membershipDetails) {

        Optional<Member> member = memberRepository.findByEmail(membershipDetails
                .getEmail());
        UUID memberId = member.map(Member::getMemberId)
                .orElseGet(() -> memberRepository.save(
                        new Member(
                                membershipDetails.getFirstName(),
                                membershipDetails.getLastName(),
                                membershipDetails.getGender(),
                                membershipDetails.getDegreeProgram(),
                                membershipDetails.getEmail(),
                                "123456"

                        ))
                        .getMemberId());

        Integer role = "member".equalsIgnoreCase(membershipDetails.getPosition())
                ? 2
                : 1;

        MemberOrganizationRole newMembership = new MemberOrganizationRole(
                memberId,
                organizationId,
                role,
                membershipDetails.getPosition(),
                membershipDetails.getYear(),
                membershipDetails.getSemester(),
                Status.active,
                membershipDetails.getCommittee());

        memberOrganizationRoleRepository.save(newMembership);

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipDetails);
    }

    @PatchMapping("/{memberId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
    public ResponseEntity<MembershipDetails> updateMemberMembership(
            @PathVariable UUID organizationId,
            @PathVariable UUID memberId,
            @RequestParam Integer year,
            @RequestParam Integer semester,
            @RequestBody MembershipDetails membershipDetails) {

        MembershipDetails membership = memberOrganizationRoleRepository
                .findMembershipDetailsForSemester(organizationId,
                        memberId,
                        year,
                        semester)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member with ID " + memberId + " is not part of organization "
                                + organizationId));

        // Update fields if provided
        if (membershipDetails.getCommittee() != null) {
            membership.setCommittee(membershipDetails.getCommittee());
        }

        if (membershipDetails.getPosition() != null) {
            membership.setPosition(membershipDetails.getPosition());
            if (!"member".equalsIgnoreCase(membership.getPosition())) {
                membership.setCommittee("Executive");
            }
        }

        if (membershipDetails.getStatus() != null) {
            membership.setStatus(membershipDetails.getStatus());
            if ("inactive".equals(membership.getStatus()) || "alumni"
                    .equals(membership.getStatus())) {
                membership.setPosition(null);
                membership.setCommittee(null);
            } else if (membership.getCommittee() == null
                    || membership.getPosition() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Active members must have both committee and position");
            }
        }

        memberOrganizationRoleRepository.updateSemesterMembershipDetails(
                organizationId,
                memberId,
                membership.getCommittee(),
                membership.getPosition(),
                membership.getStatus().toString(),
                year,
                semester);

        return ResponseEntity.ok(membership);
    }
}
