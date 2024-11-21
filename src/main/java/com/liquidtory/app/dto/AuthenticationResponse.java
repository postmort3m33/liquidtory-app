package com.liquidtory.app.dto;

public class AuthenticationResponse {

    // Vars
    private String jwt;

    // Constructor
    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
