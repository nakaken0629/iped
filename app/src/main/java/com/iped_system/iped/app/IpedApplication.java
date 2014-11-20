package com.iped_system.iped.app;

import android.app.Application;
import android.util.Log;

import com.iped_system.iped.common.Patient;
import com.iped_system.iped.common.RoleType;

import java.util.List;

/**
 * Created by kenji on 2014/08/08.
 */
public class IpedApplication extends Application {
    private static final String TAG = IpedApplication.class.getName();

    private long tokenId;
    private String userId;
    private String lastName;
    private String firstName;
    private RoleType role;
    private List<Patient> patients;
    private int index;

    public void authenticate(long tokenId, String userId, String lastName, String firstName, RoleType role, List<Patient> patients) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.role = role;
        this.patients = patients;
        this.index = 0;
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

    public List<Patient> getPatients() {
        return patients;
    }

    public String getPatientId() {
        return this.patients.get(this.index).getUserId();
    }

    public void setPatientIndex(int index) {
        this.index = index;
    }
}
