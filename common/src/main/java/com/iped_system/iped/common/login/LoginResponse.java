package com.iped_system.iped.common.login;

import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.RoleType;

/**
 * Created by kenji on 2014/08/04.
 */
public class LoginResponse extends BaseResponse {
    private long tokenId;
    private String userId;
    private String lastName;
    private String firstName;
    private RoleType role;
    private String patientId;

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
