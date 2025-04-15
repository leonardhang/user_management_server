package com.example.admin_management.model;

public class LoginResponse {

    private String token;

    private User user;

    public LoginResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
