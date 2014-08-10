package com.iped_system.iped.common;

import net.arnx.jsonic.JSONHint;

/**
 * Created by kenji on 2014/08/06.
 */
public class LoginRequest extends BaseRequest {
    private String userId;
    private String password;

    @Override
    @JSONHint(ignore=true)
    public Class<? extends BaseResponse> getResponseClass() {
        return LoginResponse.class;
    }

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
