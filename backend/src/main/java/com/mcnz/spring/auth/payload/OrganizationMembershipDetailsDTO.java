package com.mcnz.spring.auth.payload; // Or your chosen DTO/payload package

public class OrganizationMembershipDetailsDTO {
    private String position;
    private String role;

    public OrganizationMembershipDetailsDTO(String position, String role) {
        this.position = position;
        this.role = role;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}