package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.common.BaseRequest;
import com.iped_system.iped.common.BaseResponse;

import net.arnx.jsonic.JSON;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/07.
 */
public abstract class BaseServlet extends HttpServlet {
    private String userId;

    protected Map<String, Object> getCurrentUserValue() {
        if (userId == null || userId.length() == 0) {
            return null;
        }

        Query.Filter filter = new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, this.userId);
        Query query = new Query("User").setFilter(filter);
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = service.prepare(query);
        Entity user = preparedQuery.asSingleEntity();

        return user.getProperties();
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
        this.userId = (String) request.getAttribute("userId");
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
