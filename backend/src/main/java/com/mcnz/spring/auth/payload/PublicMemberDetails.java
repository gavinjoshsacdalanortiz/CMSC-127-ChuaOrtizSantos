package com.mcnz.spring.auth.payload;

import java.util.Map;
import java.util.UUID;

import com.mcnz.spring.member.Member;

public class PublicMemberDetails {
    private UUID id;

    private String firstName;

    private String lastName;

    private String gender;

    private String degreeProgram;

    private String batch;

    private String email;

    private Map<String, Map<String, String>> organizationRolesMap;

    public UUID getId() {
        return id;
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

    public String getBatch() {
        return batch;
    }

    public String getEmail() {
        return email;
    }

    public Map<String, Map<String, String>> getOrganizationRolesMap() {
        return organizationRolesMap;
    }

    public PublicMemberDetails(Member member) {
        this.id = member.getMemberId();
        this.firstName = member.getFirstName();
        this.lastName = member.getLastName();
        this.gender = member.getGender();
        this.degreeProgram = member.getDegreeProgram();
        this.batch = member.getBatch();
        this.email = member.getEmail();
        this.organizationRolesMap = member.getOrganizationRolesMap();
    }
}
