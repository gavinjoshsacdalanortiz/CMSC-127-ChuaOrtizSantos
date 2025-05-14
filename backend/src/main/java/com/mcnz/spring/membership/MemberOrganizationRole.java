package com.mcnz.spring.membership;

import java.util.UUID;

import com.mcnz.spring.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String batch;

    @Column(nullable = false)
    private String status;

    private String committee;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommittee() {
        return committee;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }
}
