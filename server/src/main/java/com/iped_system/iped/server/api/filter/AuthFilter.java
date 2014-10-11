package com.iped_system.iped.server.api.filter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

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
public class AuthFilter implements Filter {
    private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());

    class UnauthorizedException extends Exception {
        private UnauthorizedException(String message) {
            super(message);
        }

        private UnauthorizedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /* nop */
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            this.doFilterInner(request, response);
            chain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            logger.warning("unauthorized request: " + e.getMessage());
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void doFilterInner(ServletRequest request, ServletResponse response) throws IOException, ServletException, UnauthorizedException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        /* check parameters */
        long tokenId;
        try {
            tokenId = Long.valueOf(httpServletRequest.getHeader("X-IPED-TOKEN-ID"));
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("invalid token id", e);
        }

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();

        /* check token */
        Key tokenKey = KeyFactory.createKey("Token", tokenId);
        Entity token;
        try {
            token = service.get(tokenKey);
        } catch (EntityNotFoundException e) {
            throw new UnauthorizedException("no token", e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        if(calendar.before(token.getProperty("refreshDate"))) {
            throw new UnauthorizedException("expired token");
        }

        /* set authentication information */
        String userId = (String) token.getProperty("userId");
        Entity user;
        try {
            user = selectUser(userId);
            if (user == null) {
                throw new UnauthorizedException("no user");
            }
        } catch(PreparedQuery.TooManyResultsException e) {
            throw new UnauthorizedException("duplicate users + " + userId, e);
        }

        /* refresh token */
        token.setProperty("refreshDate", new Date());
        service.put(token);

        /* store user information in a request attribute */
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(userId);
        authInfo.setFirstName((String) user.getProperty("firstName"));
        authInfo.setLastName((String) user.getProperty("lastName"));
        authInfo.setPatientId((String) user.getProperty("patientId"));
        request.setAttribute("authInfo", authInfo);
    }

    private Entity selectUser(String userId) {
        Query.Filter filter = new Query.FilterPredicate("userId", Query.FilterOperator.EQUAL, userId);
        Query query = new Query("User").setFilter(filter);
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = service.prepare(query);
        Entity entity = preparedQuery.asSingleEntity();
        return entity;
    }

    @Override
    public void destroy() {
        /* nop */
    }
}
