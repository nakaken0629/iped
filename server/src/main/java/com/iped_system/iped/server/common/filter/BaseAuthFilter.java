package com.iped_system.iped.server.common.filter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.iped_system.iped.server.api.filter.AuthInfo;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.User;

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

/**
 * Created by kenji on 2014/11/08.
 */
public abstract class BaseAuthFilter implements Filter {
    private static final Logger logger = Logger.getLogger(BaseAuthFilter.class.getName());
    private static final int TOKEN_EXPIRE_TIME = 30 * 60 * 1000;

    public static final String TOKEN_EXPIRE_KEY = BaseAuthFilter.class.getName() + ":TOKEN_EXPIRE_KEY";
    public static final String AUTH_INFO_KEY = BaseAuthFilter.class.getName() + ":AUTH_INFO_KEY";

    public class UnauthorizedException extends Exception {
        public UnauthorizedException(String message) {
            super(message);
        }

        public UnauthorizedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /* nop */
    }

    protected abstract long getTokenId(ServletRequest request) throws UnauthorizedException;

    protected abstract String getPatientId(ServletRequest request) throws UnauthorizedException;

    protected abstract void onUnauthorized(ServletResponse response) throws IOException;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            this.doFilterInner(request);
            chain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            logger.warning("unauthorized request: " + e.getMessage());
            onUnauthorized(response);
        }
    }

    protected void doFilterInner(ServletRequest request) throws IOException, ServletException, UnauthorizedException {
        /* check parameters */
        long tokenId = getTokenId(request);

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
        calendar.add(Calendar.MILLISECOND, -TOKEN_EXPIRE_TIME);
        if (calendar.getTime().compareTo((Date) token.getProperty(TOKEN_EXPIRE_KEY)) >= 0) {
            throw new UnauthorizedException("expired token");
        }

        /* set authentication information */
        String userId = (String) token.getProperty("userId");
        User user;
        try {
            user = UserDomain.getInstance().getUser(userId);
            if (user == null) {
                throw new UnauthorizedException("no user");
            }
        } catch (PreparedQuery.TooManyResultsException e) {
            throw new UnauthorizedException("duplicate users + " + userId, e);
        }

        /* refresh token */
        token.setProperty(TOKEN_EXPIRE_KEY, new Date());
        service.put(token);

        /* store user information in a request attribute */
        AuthInfo authInfo = new AuthInfo(user, getPatientId(request));
        request.setAttribute(AUTH_INFO_KEY, authInfo);
    }

    @Override
    public void destroy() {
        /* nop */
    }
}
