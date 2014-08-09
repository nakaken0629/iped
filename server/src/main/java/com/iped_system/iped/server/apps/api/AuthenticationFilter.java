package com.iped_system.iped.server.apps.api;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/09.
 */
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /* nop */
    }

    private void sendForbidden(ServletResponse response) throws IOException {
        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long keyId;
        try {
            keyId = Long.valueOf(request.getParameter("keyId"));
        } catch (NumberFormatException e) {
            sendForbidden(response);
            return;
        }
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Key tokenKey = KeyFactory.createKey("token", keyId);
        Entity token;
        try {
            token = service.get(tokenKey);
        } catch (EntityNotFoundException e) {
            sendForbidden(response);
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        if(!calendar.before(token.getProperty("refreshDate"))) {
            sendForbidden(response);
            return;
        }
        token.setProperty("refreshDate", new Date());
        service.put(token);
    }

    @Override
    public void destroy() {
        /* nop */
    }
}
