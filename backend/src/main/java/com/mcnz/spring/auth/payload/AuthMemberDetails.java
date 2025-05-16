package com.mcnz.spring.auth.payload;

import java.util.Map;
import java.util.UUID;

import com.mcnz.spring.member.Member;

public class AuthMemberDetails {
    private UUID member_id;

    private String firstName;

    private String lastName;

    private String gender;

    private String degreeProgram;

    private String email;

    Map<String, Map<String, String>> organizationRolesMap;

    public AuthMemberDetails(Member member) {
        this.member_id = member.getMemberId();
        this.firstName = member.getFirstName();
        this.lastName = member.getLastName();
        this.gender = member.getGender();
        this.degreeProgram = member.getDegreeProgram();
        this.email = member.getEmail();
        this.organizationRolesMap = member.getOrganizationRolesMap();
    }

    public UUID getMember_id() {
        return member_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getDegreeProgram() {
        return degreeProgram;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, Map<String, String>> getOrganizationRolesMap() {
        return organizationRolesMap;
    }
}
