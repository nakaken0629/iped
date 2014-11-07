package com.iped_system.iped.server.web.servlet;

import com.iped_system.iped.server.domain.UserDomain;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/11/07.
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatchSelf(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        UserDomain.LoginResult result = UserDomain.getInstance().login(userId, password);

        if (result != null) {
            resp.sendRedirect("/web/secure/main");
        } else {
            req.setAttribute("userId", userId);
            req.setAttribute("globalError", true);
            dispatchSelf(req, resp);
        }
    }

    private void dispatchSelf(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/web/login.jsp");
        dispatcher.forward(req, resp);
    }
}