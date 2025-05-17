package com.mcnz.spring.membership;

import java.util.UUID;

import com.mcnz.spring.status.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MemberOrganizationRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID member_id;

    @Column(nullable = false)
    private UUID organization_id;

    private Integer role_id;

    private String position;

    @Column(nullable = false)
    private Integer batch;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private Status status;

    private String committee;

    public MemberOrganizationRole(UUID member_id, UUID organization_id, Integer role_id, String position, Integer batch,
            Integer year, Integer semester,
            Status status, String committee) {
        this.member_id = member_id;
        this.organization_id = organization_id;
        this.role_id = role_id;
        this.position = position;
        this.batch = batch;
        this.year = year;
        this.semester = semester;
        this.status = status;
        this.committee = committee;
    }

    public UUID getMemberId() {
        return member_id;
    }

    public UUID getOrganizationId() {
        return organization_id;
    }

    public Integer getRole() {
        return role_id;
    }

    public void setRole(Integer role_id) {
        this.role_id = role_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCommittee() {
        return committee;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
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

}