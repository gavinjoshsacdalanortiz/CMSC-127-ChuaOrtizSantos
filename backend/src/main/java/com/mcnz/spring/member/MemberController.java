package com.mcnz.spring.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.mcnz.spring.auth.payload.PublicMemberDetails;
import com.mcnz.spring.membership.MemberOrganizationRole;
import com.mcnz.spring.membership.MemberOrganizationRoleRepository;
// import com.mcnz.spring.organization.OrganizationRepository;
import com.mcnz.spring.role.RoleRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController
@RequestMapping("/organization/{organizationId}/members")
public class MemberController {

        private final MemberOrganizationRoleRepository memberOrganizationRoleRepository;

        @Autowired
        public MemberController(MemberRepository memberRepository,
                        MemberOrganizationRoleRepository memberOrganizationRoleRepository,
                        // OrganizationRepository organizationRepository,
                        RoleRepository roleRepository) {
                this.memberOrganizationRoleRepository = memberOrganizationRoleRepository;
                // this.organizationRepository = organizationRepository;

        }

        @GetMapping
        @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
        public ResponseEntity<List<PublicMemberDetails>> getAllMembersInOrganization(
                        @PathVariable UUID organizationId) {
                // if (!organizationRepository.existsById(organizationId)) {
                // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                // "Organization not found with ID: " + organizationId);
                // }

                List<MemberOrganizationRole> memberships = memberOrganizationRoleRepository
                                .findByOrganizationId(organizationId);

                List<PublicMemberDetails> members = memberships.stream()
                                .map(membership -> new PublicMemberDetails(membership.getMember()))
                                .collect(Collectors.toList());

                return ResponseEntity.ok(members);
        }

        @GetMapping("/{memberId}")
        @PreAuthorize("hasAuthority('ROLE_MEMBER_ORG_' + #organizationId) or hasAuthority('ROLE_ADMIN_ORG_' + #organizationId)")
        public ResponseEntity<PublicMemberDetails> getMemberInOrganization(@PathVariable UUID organizationId,
                        @PathVariable UUID memberId) {
                // if (!organizationRepository.existsById(organizationId)) {
                // throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                // "Organization not found with ID: " + organizationId);
                // }

                Optional<MemberOrganizationRole> membershipOpt = memberOrganizationRoleRepository
                                .findByMemberIdAndOrganizationId(memberId, organizationId);

                if (membershipOpt.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Member with ID " + memberId + " is not part of organization " + organizationId
                                                        + " or member not found.");
                }

                return ResponseEntity.ok(new PublicMemberDetails(membershipOpt.get().getMember()));
        }

}