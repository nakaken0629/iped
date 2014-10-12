package com.iped_system.iped.server.backend.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.server.domain.model.User;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/09.
 */
public class UsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* prepare users */
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("User");
        PreparedQuery pq = service.prepare(query);
        ArrayList<User> users = new ArrayList<User>();
        for(Entity entity : pq.asIterable()) {
            User user = new User(entity);
            users.add(user);
        }
        req.setAttribute("users", users);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/users.jsp");
        dispatcher.forward(req, resp);
    }
}
