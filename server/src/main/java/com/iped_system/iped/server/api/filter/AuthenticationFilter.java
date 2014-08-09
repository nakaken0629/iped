package com.iped_system.iped.server.api.filter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

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
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /* nop */
    }

    private void sendUnAuthorized(ServletResponse response, String reason) throws IOException {
        logger.warning("unauthorized request: " + reason);
        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        response.flushBuffer();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long tokenId;
        try {
            tokenId = Long.valueOf(request.getParameter("tokenId"));
        } catch (NumberFormatException e) {
            sendUnAuthorized(response, "invalid token id");
            return;
        }
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Key tokenKey = KeyFactory.createKey("token", tokenId);
        Entity token;
        try {
            token = service.get(tokenKey);
        } catch (EntityNotFoundException e) {
            sendUnAuthorized(response, "no token");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        if(calendar.before(token.getProperty("refreshDate"))) {
            sendUnAuthorized(response, "expired token");
            return;
        }
        token.setProperty("refreshDate", new Date());
        service.put(token);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        /* nop */
    }
}
