package com.mcnz.spring.auth.payload;

import jakarta.validation.constraints.NotBlank;

public class UserDetailsRequest {
    @NotBlank
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }
}