package com.iped_system.iped;

import android.app.Application;

import com.iped_system.iped.common.LoginResponse;

/**
 * Created by kenji on 2014/08/08.
 */
public class IpedApplication extends Application {
    private long tokenId;
    private String userId;
    private String lastName;
    private String firstName;

    public void authorize(LoginResponse response) {
        this.tokenId = response.getTokenId();
        this.userId = response.getUserId();
        this.lastName = response.getLastName();
        this.firstName = response.getFirstName();
    }

    public long getTokenId() {
        return tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
