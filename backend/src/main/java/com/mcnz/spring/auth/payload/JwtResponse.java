package com.mcnz.spring.auth.payload;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UUID id;
    private String username;
    private String email;
    private Map<String, List<String>> organizationRoles;

    public JwtResponse(String token, UUID id, String username, String email,
            Map<String, List<String>> organizationRoles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.organizationRoles = organizationRoles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, List<String>> getOrganizationRoles() {
        return organizationRoles;
    }
}