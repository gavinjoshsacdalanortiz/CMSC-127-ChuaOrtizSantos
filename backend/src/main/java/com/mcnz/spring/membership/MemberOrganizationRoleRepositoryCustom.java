package com.mcnz.spring.membership;

import java.util.List;
import java.util.UUID;

import com.mcnz.spring.membership.payload.MembershipDetails;

public interface MemberOrganizationRoleRepositoryCustom {
    List<MembershipDetails> findMembersByOrganizationIdWithFilters(UUID organizationId, String position,
            String committee, String status,
            String gender, String degreeProgram, Integer batch,
            Integer batchStart,
            Integer batchEnd, Integer year,
            Integer yearStart,
            Integer yearEnd, Integer semester,
            Integer semesterStart,
            Integer semesterEnd, String sortBy, String sortDirection);
}
