package com.iped_system.iped.server.backend;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.server.domain.model.User;

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
        user.setRole(req.getParameter("role"));
        user.setPatientId(req.getParameter("patientId"));
    }
}
