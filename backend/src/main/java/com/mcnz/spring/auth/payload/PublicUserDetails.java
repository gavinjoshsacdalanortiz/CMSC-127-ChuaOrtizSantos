package com.mcnz.spring.auth.payload;

import com.mcnz.spring.user.User;

import java.util.Map;
import java.util.UUID;

public class PublicUserDetails {
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

    public PublicUserDetails(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
        this.degreeProgram = user.getDegreeProgram();
        this.batch = user.getBatch();
        this.email = user.getEmail();
        this.organizationRolesMap = user.getOrganizationRolesMap();
    }
}
