package com.iped_system.iped.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.iped_system.iped.common.RoleType;

/**
 * Created by kenji on 2014/08/08.
 */
public class IpedApplication extends Application {
    private long tokenId;
    private String userId;
    private String lastName;
    private String firstName;
    private RoleType role;
    private String patientId;

    public void authenticate(long tokenId, String userId, String lastName, String firstName, RoleType role, String patientId) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.patientId = patientId;
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

    public RoleType getRole() {
        return role;
    }

    public String getPatientId() {
        return patientId;
    }
}
