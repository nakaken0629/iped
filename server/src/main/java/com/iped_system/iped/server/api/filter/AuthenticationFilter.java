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
import javax.servlet.http.HttpServletRequest;
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
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        /* check parameters */
        long tokenId;
        try {
            tokenId = Long.valueOf(httpServletRequest.getHeader("X-IPED-TOKEN-ID"));
        } catch (NumberFormatException e) {
            sendUnAuthorized(response, "invalid token id");
            return;
        }
        String userId = httpServletRequest.getHeader("X-IPED-USER-ID");
        if (userId == null || userId.length() == 0) {
            sendUnAuthorized(response, "userId is not exist");
        }

        /* check token */
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Key tokenKey = KeyFactory.createKey("token", tokenId);
        Entity token;
        try {
            token = service.get(tokenKey);
        } catch (EntityNotFoundException e) {
            sendUnAuthorized(response, "no token");
            return;
        }
        if(!userId.equals(token.getProperty("userId"))) {
            sendUnAuthorized(response, "different userId");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        if(calendar.before(token.getProperty("refreshDate"))) {
            sendUnAuthorized(response, "expired token");
            return;
        }

        /* refresh token */
        token.setProperty("refreshDate", new Date());
        service.put(token);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        /* nop */
    }
}
