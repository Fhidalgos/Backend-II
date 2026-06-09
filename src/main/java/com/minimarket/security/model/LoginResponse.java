package com.minimarket.security.model;

public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String rol;

    // Constructor
    public LoginResponse(String token, String username, String rol) {
        this.token = token;
        this.username = username;
        this.rol = rol;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}