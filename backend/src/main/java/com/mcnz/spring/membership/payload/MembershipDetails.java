package com.mcnz.spring.membership.payload;

import java.util.UUID;

public class MembershipDetails {

    private UUID member_id;

    private String firstName;

    private String lastName;

    private String gender;

    private String degreeProgram;

    private String email;

    private String batch;

    private String committee;

    private String position;

    private String status;

    public UUID getMemberId() {
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

    public String getBatch() {
        return batch;
    }

    public String getEmail() {
        return email;
    }

    public String getCommittee() {
        return committee;
    }

    public String getPosition() {
        return position;
    }

    public String getStatus() {
        return status;
    }

    public MembershipDetails(UUID member_id, String firstName, String lastName, String gender, String degreeProgram,
            String email) {
        this.member_id = member_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.degreeProgram = degreeProgram;
        this.email = email;
    }

    public MembershipDetails(UUID member_id, String firstName, String lastName, String gender, String degreeProgram,
            String email, String batch, String committee, String position, String status) {
        this.member_id = member_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.degreeProgram = degreeProgram;
        this.email = email;
        this.batch = batch;
        this.committee = committee;
        this.position = position;
        this.status = status;
    }

}
