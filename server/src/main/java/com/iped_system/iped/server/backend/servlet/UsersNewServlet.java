package com.iped_system.iped.server.backend.servlet;

import com.iped_system.iped.common.RoleType;
import com.iped_system.iped.server.domain.model.User;
import com.iped_system.iped.server.backend.UserUtils;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/09.
 */
public class UsersNewServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UsersNewServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User();
        req.setAttribute("user", user);
        req.setAttribute("roles", RoleType.getRoles());

        dispatchSelf(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = UserUtils.createFromRequest(req);
        req.setAttribute("user", user);
        req.setAttribute("roles", RoleType.getRoles());

        if (!user.isValid()) {
            dispatchSelf(req, resp);
            return;
        }

        UserUtils.insert(user);
        resp.sendRedirect("/backend/users");
    }

    private void dispatchSelf(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/new-user.jsp");
        dispatcher.forward(req, resp);
    }
}
