package com.liquidtory.app.dto;

public class UserChangePasswordRequest {

    // Vars
    private Long userId;
    private String password1;
    private String password2;

    //constructors
    public UserChangePasswordRequest() {
    }

    public UserChangePasswordRequest(Long userId, String password1, String password2) {
        this.userId = userId;
        this.password1 = password1;
        this.password2 = password2;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getPassword1() { return password1; }

    public void setPassword1(String password1) { this.password1 = password1; }

    public String getPassword2() { return password2; }

    public void setPassword2(String password2) { this.password2 = password2; }
}
