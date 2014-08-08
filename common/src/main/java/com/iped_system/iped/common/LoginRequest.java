package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/08/06.
 */
public class LoginRequest extends BaseRequest {
    private String userId;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
