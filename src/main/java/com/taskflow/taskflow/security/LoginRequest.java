package com.taskflow.taskflow.security;


/*
@Data
public class LoginRequest {
    private String email;
    private String password;
}*/                 //pb lombook

public class LoginRequest {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
