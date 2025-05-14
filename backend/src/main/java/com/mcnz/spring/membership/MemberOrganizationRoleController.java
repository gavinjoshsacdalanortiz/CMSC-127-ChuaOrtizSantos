package com.mcnz.spring.membership;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.mcnz.spring.membership.payload.MembershipDetails;

@RestController
@RequestMapping("/membership/{organizationId}/members")
public class MemberOrganizationRoleController {
        private final MemberOrganizationRoleRepository memberOrganizationRoleRepository;

        @Autowired
        public MemberOrganizationRoleController(
                        MemberOrganizationRoleRepository memberOrganizationRoleRepository) {
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

                int rows = memberOrganizationRoleRepository.deleteByMemberIdAndOrganizationId(organizationId, memberId);

                return ResponseEntity.ok("Successfully deleted!");
        }

        // @PutMapping("/{memberId}")
        // @PreAuthorize("hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
        // public ResponseEntity<?> deleteMemberInOrganization(@PathVariable UUID
        // organizationId,
        // @PathVariable UUID memberId) {
        // // if (!organizationRepository.existsById(organizationId)) {
        // // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
        // // "Organization not found with ID: " + organizationId);
        // // }

        // Optional<MembershipDetails> membership = memberOrganizationRoleRepository
        // .findByMemberIdAndOrganizationId(organizationId, memberId);

        // if (membership.isEmpty()) {
        // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
        // "Member with ID " + memberId + " is not part of organization " +
        // organizationId
        // + " or member not found.");
        // }

        // int rows =
        // memberOrganizationRoleRepository.deleteByMemberIdAndOrganizationId(organizationId,
        // memberId);

        // return ResponseEntity.ok("Successfully deleted!");
        // }
}
