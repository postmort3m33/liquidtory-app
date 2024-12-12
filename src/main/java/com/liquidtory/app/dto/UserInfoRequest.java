package com.liquidtory.app.dto;

public class UserInfoRequest {

    // Name
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Long companyId;
    private String role;

    // Constrcutror
    public UserInfoRequest() {
    }

    public UserInfoRequest(String firstName, String lastName, String username, String password, Long companyId, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.companyId = companyId;
        this.role = role;
    }

    // /Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Long getCompanyId() { return companyId; }

    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }
}
