package com.iped_system.iped.server.api.filter;

import com.iped_system.iped.common.RoleType;
import com.iped_system.iped.server.domain.model.User;

import java.util.List;

/**
 * Created by kenji on 2014/10/11.
 */
public class AuthInfo {
    private User user;
    private String patientId;

    public AuthInfo(User user, String patientId) {
        this.user = user;
        this.patientId = patientId;
    }

    public String getUserId() {
        return this.user.getUserId();
    }

    public RoleType getRole() {
        return this.user.getRole();
    }

    public List<String> getPatientIdList() {
        return this.user.getPatientIdList();
    }

    public String getPatientId() {
        return this.patientId;
    }
}
