package com.mcnz.spring.auth.payload;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserDetailsResponse {
    private UUID id;
    private String email;
    private Map<String, List<String>> organizationRoles;

    public UserDetailsResponse(UUID id, String email, Map<String, List<String>> organizationRoles){
        this.id = id;
        this.email = email;
        this.organizationRoles = organizationRoles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, List<String>> getOrganizationRoles() {
        return organizationRoles;
    }
}

