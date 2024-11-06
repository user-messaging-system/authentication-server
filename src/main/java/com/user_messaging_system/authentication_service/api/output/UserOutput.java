package com.user_messaging_system.authentication_service.api.output;

import java.util.List;

public class UserOutput{
    private String id;
    private String email;
    private String name;
    private String lastName;
    private String password;
    private List<String> roles;

    public UserOutput(String email, String name, String lastName, String password, List<String> roles) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}