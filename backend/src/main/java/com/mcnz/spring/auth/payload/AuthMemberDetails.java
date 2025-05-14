package com.mcnz.spring.auth.payload;

import java.util.Map;

import com.mcnz.spring.member.Member;
import com.mcnz.spring.membership.payload.MembershipDetails;

public class AuthMemberDetails extends MembershipDetails {
    private Map<String, Map<String, String>> organizationRolesMap;

    public AuthMemberDetails(Member member) {
        super(member.getMemberId(), member.getFirstName(), member.getLastName(), member.getGender(),
                member.getDegreeProgram(), member.getEmail());
        this.organizationRolesMap = member.getOrganizationRolesMap();
    }

    public Map<String, Map<String, String>> getOrganizationRolesMap() {
        return organizationRolesMap;
    }
}
