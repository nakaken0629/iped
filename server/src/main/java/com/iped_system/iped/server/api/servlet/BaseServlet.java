package com.iped_system.iped.server.api.servlet;

import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;
import com.iped_system.iped.server.api.filter.AuthInfo;

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
    private AuthInfo authInfo;

    protected AuthInfo getAuthInfo() {
        return this.authInfo;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doService(request, response);
    }

    private void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.authInfo = (AuthInfo) request.getAttribute("authInfo");
        String parameter = request.getParameter("parameter");
        BaseRequest baseRequest = JSON.decode(parameter, getRequestClass());
        BaseResponse baseResponse = execute(baseRequest);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(baseResponse.toJSON());
    }

    protected abstract Class<? extends BaseRequest> getRequestClass();
    protected abstract BaseResponse execute(BaseRequest baseRequest);
}
