package com.mcnz.spring.membership;

import com.mcnz.spring.member.Member;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mcnz.spring.member.MemberRepository;
import com.mcnz.spring.membership.payload.MembershipDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/membership/{organizationId}/members")
public class MemberOrganizationRoleController {
        private final MemberOrganizationRoleRepository memberOrganizationRoleRepository;
        private final MemberRepository memberRepository;

        @Autowired
        public MemberOrganizationRoleController(
                        MemberOrganizationRoleRepository memberOrganizationRoleRepository,
                        MemberRepository memberRepository) {
                this.memberRepository = memberRepository;
                this.memberOrganizationRoleRepository = memberOrganizationRoleRepository;
        }

        @GetMapping
        @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
        public ResponseEntity<List<MembershipDetails>> getAllMembersInOrganization(
                        @PathVariable UUID organizationId) {
                // if (!organizationRepository.existsById(organizationId)) {
                // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                // "Organization not found with ID: " + organizationId);
                // }

                List<MembershipDetails> memberships = memberOrganizationRoleRepository
                                .findMembersByOrganizationId(organizationId);

                return ResponseEntity.ok(memberships);
        }

        @GetMapping("/{memberId}")
        @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
        public ResponseEntity<MembershipDetails> getMemberInOrganization(@PathVariable UUID organizationId,
                        @PathVariable UUID memberId) {
                // if (!organizationRepository.existsById(organizationId)) {
                // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                // "Organization not found with ID: " + organizationId);
                // }

                Optional<MembershipDetails> membership = memberOrganizationRoleRepository
                                .findByMemberIdAndOrganizationId(organizationId, memberId);

                if (membership.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Member with ID " + memberId + " is not part of organization " +
                                                        organizationId
                                                        + " or member not found.");
                }

                return ResponseEntity.ok(membership.get());
        }

        @DeleteMapping("/{memberId}")
        @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
        public ResponseEntity<?> deleteMemberInOrganization(@PathVariable UUID organizationId,
                        @PathVariable UUID memberId) {
                // if (!organizationRepository.existsById(organizationId)) {
                // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                // "Organization not found with ID: " + organizationId);
                // }

                Optional<MembershipDetails> membership = memberOrganizationRoleRepository
                                .findByMemberIdAndOrganizationId(organizationId, memberId);

                if (membership.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Member with ID " + memberId + " is not part of organization " +
                                                        organizationId
                                                        + " or member not found.");
                }

                memberOrganizationRoleRepository.deleteByMemberIdAndOrganizationId(organizationId, memberId);

                return ResponseEntity.ok("Successfully deleted!");
        }

        @PutMapping
        @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
        public ResponseEntity<?> addMemberToOrganization(@PathVariable UUID organizationId,
                        @Valid @RequestBody MembershipDetails membershipDetails) {
                // if (!organizationRepository.existsById(organizationId)) {
                // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                // "Organization not found with ID: " + organizationId);
                // }

                Optional<Member> member = memberRepository
                                .findByEmail(membershipDetails.getEmail());

                int role = membershipDetails.getPosition().toLowerCase().equals("member") ? 1 : 2;
                UUID memberId;

                if (member.isEmpty()) {
                        Member newMember = memberRepository.save(new Member(membershipDetails.getFirstName(),
                                        membershipDetails.getLastName(), membershipDetails.getGender(),
                                        membershipDetails.getDegreeProgram(), membershipDetails.getEmail(), "123456"));
                        memberId = newMember.getMemberId();
                } else {
                        memberId = member.get().getMemberId();
                }

                memberOrganizationRoleRepository.save(new MemberOrganizationRole(memberId, organizationId, role,
                                membershipDetails.getPosition(), membershipDetails.getBatch(), "active",
                                membershipDetails.getCommittee()));

                return ResponseEntity.ok("Successfully updated!");
        }

        /*
         * These are the only updatable fields
         * this.committee = committee;
         * this.position = position;
         * this.status = status;
         */
        @PatchMapping("/{memberId}")
        @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
        public ResponseEntity<?> updateMemberInOrganization(@PathVariable UUID organizationId,
                        @PathVariable UUID memberId, @RequestBody MembershipDetails membershipDetails) {
                // if (!organizationRepository.existsById(organizationId)) {
                // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                // "Organization not found with ID: " + organizationId);
                // }

                MembershipDetails membership = memberOrganizationRoleRepository
                                .findByMemberIdAndOrganizationId(organizationId, memberId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Member with ID " + memberId + " is not part of organization " +
                                                                organizationId
                                                                + " or member not found."));

                if (membershipDetails.getCommittee() != null) {
                        membership.setCommittee(membershipDetails.getCommittee());
                }

                if (membershipDetails.getPosition() != null) {
                        membership.setPosition(membershipDetails.getPosition());
                        if (!membership.getPosition().toLowerCase().equals("member")) {
                                membership.setCommittee("Executive");
                        }
                }

                if (membershipDetails.getStatus() != null) {
                        membership.setStatus(membershipDetails.getStatus());
                        if (membership.getStatus().equals("inactive") || membership.getStatus().equals("alumni")) {
                                membership.setPosition(null);
                                membership.setCommittee(null);
                        } else {
                                if (membership.getCommittee() == null && membership.getCommittee() == null)
                                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                        "Member with ID " + memberId
                                                                        + " cannot be active with organization " +
                                                                        organizationId
                                                                        + " while having no committee/position.");
                        }
                }

                memberOrganizationRoleRepository.updateByMemberIdAndOrganizationId(organizationId, memberId,
                                membership.getCommittee(), membership.getPosition(), membership.getStatus());

                return ResponseEntity.ok("Successfully updated!");
        }
}
