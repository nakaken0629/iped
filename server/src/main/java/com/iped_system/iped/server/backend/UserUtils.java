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

    private static Entity selectByUserId(String userId) {
        Query.Filter filter = new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, userId);
        Query query = new Query("User").setFilter(filter);
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = service.prepare(query);
        Entity entity = preparedQuery.asSingleEntity();
        return entity;
    }

    public static User createFromUserId(String userId) {
        Entity entity = selectByUserId(userId);
        User user = new User();
        user.setUserId((String) entity.getProperty("userId"));
        user.setLastName((String) entity.getProperty("lastName"));
        user.setFirstName((String) entity.getProperty("firstName"));
        user.setRole((String) entity.getProperty("role"));
        user.setPatientId((String) entity.getProperty("patientId"));
        return user;
    }

    public static User createFromRequest(HttpServletRequest req) {
        User user = new User();
        user.setUserId(req.getParameter("userId"));
        user.setLastName(req.getParameter("lastName"));
        user.setFirstName(req.getParameter("firstName"));
        user.setPassword(req.getParameter("password"));
        user.setRole(req.getParameter("role"));
        user.setPatientId(req.getParameter("patientId"));
        return user;
    }

    public static void insert(User user) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("User");
        entity.setProperty("userId", user.getUserId());
        entity.setProperty("lastName", user.getLastName());
        entity.setProperty("firstName", user.getFirstName());
        entity.setProperty("password", user.getPassword());
        entity.setProperty("role", user.getRole());
        entity.setProperty("patientId", user.getPatientId());
        service.put(entity);
    }

    public static void update(User user) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity entity = selectByUserId(user.getUserId());
        entity.setProperty("lastName", user.getLastName());
        entity.setProperty("firstName", user.getFirstName());
        entity.setProperty("password", user.getPassword());
        entity.setProperty("role", user.getRole());
        entity.setProperty("patientId", user.getPatientId());
        service.put(entity);
    }

    public static void delete(String userId) {
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity entity = selectByUserId(userId);
        service.delete(entity.getKey());
    }
}
