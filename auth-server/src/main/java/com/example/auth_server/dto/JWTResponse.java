package com.example.auth_server.dto;

public class JWTResponse {
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
