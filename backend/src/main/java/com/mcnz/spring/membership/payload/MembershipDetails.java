package com.mcnz.spring.membership.payload;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class MembershipDetails {

    private UUID member_id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String gender;

    @NotBlank
    private String degreeProgram;

    @NotBlank
    private String email;

    @NotBlank
    private Integer batch;

    private Integer year;

    private Integer semester;

    @NotBlank
    private String committee;

    @NotBlank
    private String position;

    private String status;

    public UUID getMember_id() {
        return member_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDegreeProgram() {
        return degreeProgram;
    }

    public void setDegreeProgram(String degreeProgram) {
        this.degreeProgram = degreeProgram;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getCommittee() {
        return committee;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MembershipDetails() {

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
            String email, Integer batch, Integer year, Integer semester, String committee, String position,
            String status) {
        this.member_id = member_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.degreeProgram = degreeProgram;
        this.email = email;
        this.batch = batch;
        this.year = year;
        this.semester = semester;
        this.committee = committee;
        this.position = position;
        this.status = status;
    }

}
