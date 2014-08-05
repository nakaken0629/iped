package com.iped_system.iped.common;

/**
 * Created by kenji on 2014/08/06.
 */
public class LoginRequest extends BaseRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
