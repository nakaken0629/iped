package com.iped_system.iped.server.apps.api;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.common.LoginRequest;

import net.arnx.jsonic.JSON;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/07.
 */
public abstract class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    private void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parameter = request.getParameter("parameter");
        BaseRequest baseRequest = JSON.decode(parameter, LoginRequest.class);
        BaseResponse baseResponse = execute(baseRequest);

        response.setContentType("application/json");
        response.getWriter().print(baseResponse.toJSON());
    }

    protected abstract BaseResponse execute(BaseRequest baseRequest);
}
