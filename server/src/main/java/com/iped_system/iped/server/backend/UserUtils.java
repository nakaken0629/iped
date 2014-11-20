package com.iped_system.iped.server.backend;

import com.iped_system.iped.common.RoleType;
import com.iped_system.iped.server.domain.model.User;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kenji on 2014/08/24.
 */
public final class UserUtils {
    private UserUtils() {
        /* nop */
    }

    public static void createFromRequest(User user, HttpServletRequest req) {
        user.setUserId(req.getParameter("userId"));
        user.setLastName(req.getParameter("lastName"));
        user.setFirstName(req.getParameter("firstName"));
        user.setPassword(req.getParameter("password"));
        user.setRole(RoleType.valueOf(req.getParameter("role")));
        List<String> patients = Arrays.asList(req.getParameter("patientIdList").split(","));
        user.setPatientIdList(patients);
    }
}
