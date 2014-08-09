package com.iped_system.iped.server.backend.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.iped_system.iped.server.backend.model.User;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/09.
 */
public class NewUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User();
        req.setAttribute("user", user);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/new-user.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User();
        user.setUserId(req.getParameter("userId"));
        user.setLastName(req.getParameter("lastName"));
        user.setFirstName(req.getParameter("firstName"));
        user.setPassword(req.getParameter("password"));
        req.setAttribute("user", user);

        if(!user.isValid()) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/new-user.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("User");
        entity.setProperty("userId", user.getUserId());
        entity.setProperty("lastName", user.getLastName());
        entity.setProperty("firstName", user.getFirstName());
        entity.setProperty("password", user.getPassword());
        service.put(entity);

        resp.sendRedirect("/backend/users");
    }
}
