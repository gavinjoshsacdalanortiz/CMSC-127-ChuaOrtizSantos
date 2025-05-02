package com.mcnz.spring.membership;

import java.util.UUID;

import com.mcnz.spring.organization.Organization;
import com.mcnz.spring.roles.Role;
import com.mcnz.spring.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class UserOrganizationRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Organization organization;

    @ManyToOne
    private Role role;

    public User getUser() {
        return user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Role getRole() {
        return role;
    }

}
