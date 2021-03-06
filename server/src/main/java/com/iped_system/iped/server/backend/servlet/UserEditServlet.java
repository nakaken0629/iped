package com.iped_system.iped.server.backend.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.iped_system.iped.common.RoleType;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.User;
import com.iped_system.iped.server.backend.UserUtils;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/09.
 */
public class UserEditServlet extends HttpServlet {
    private DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getPathInfo().split("/")[1];
        UserDomain domain = UserDomain.getInstance();
        User user = domain.getUser(userId);
        req.setAttribute("user", user);
        req.setAttribute("roles", RoleType.values());

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/edit-user.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("register") != null) {
            doPostAsRegister(req, resp);
        } else if (req.getParameter("delete") != null) {
            doPostAsDelete(req, resp);
        }
    }

    private void doPostAsRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDomain domain = UserDomain.getInstance();
        User user = domain.getUser(req.getParameter("userId"));
        UserUtils.createFromRequest(user, req);
        req.setAttribute("user", user);
        req.setAttribute("roles", RoleType.values());

        if (!user.isValid()) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/edit-user.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        user.save(this.datastoreService);
        resp.sendRedirect("/backend/users");
    }

    private void doPostAsDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        UserDomain domain = UserDomain.getInstance();
        User user = domain.getUser(userId);
        user.delete(this.datastoreService);
        resp.sendRedirect("/backend/users");
    }
}
