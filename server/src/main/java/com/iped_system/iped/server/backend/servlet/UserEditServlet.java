package com.iped_system.iped.server.backend.servlet;

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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getPathInfo().split("/")[1];
        UserDomain domain = UserDomain.getInstance();
        User user = domain.getByUserId(userId);
        req.setAttribute("user", user);
        req.setAttribute("roles", RoleType.getRoles());

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
        User user = UserUtils.createFromRequest(req);
        req.setAttribute("user", user);
        req.setAttribute("roles", RoleType.getRoles());

        if (!user.isValid()) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/edit-user.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        UserUtils.update(user);
        resp.sendRedirect("/backend/users");
    }

    private void doPostAsDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        UserUtils.delete(userId);
        resp.sendRedirect("/backend/users");
    }
}
