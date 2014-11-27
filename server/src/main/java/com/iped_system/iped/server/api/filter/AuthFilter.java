package com.iped_system.iped.server.api.filter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.server.common.filter.BaseAuthFilter;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/09.
 */
public class AuthFilter extends BaseAuthFilter {
    private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());

    @Override
    protected long getTokenId(ServletRequest request) throws UnauthorizedException {
        HttpServletRequest req = (HttpServletRequest) request;

        try {
            return Long.valueOf(req.getHeader("X-IPED-TOKEN-ID"));
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("invalid token id", e);
        }
    }

    @Override
    protected String getPatientId(ServletRequest request) throws UnauthorizedException {
        HttpServletRequest req = (HttpServletRequest) request;
        return req.getHeader("X-IPED-PATIENT-ID");
    }

    @Override
    protected void onUnauthorized(ServletResponse response) throws IOException {
        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
