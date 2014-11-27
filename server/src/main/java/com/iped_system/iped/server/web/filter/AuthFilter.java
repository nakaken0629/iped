package com.iped_system.iped.server.web.filter;

import com.iped_system.iped.server.common.filter.BaseAuthFilter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/11/08.
 */
public class AuthFilter extends BaseAuthFilter {
    private static final Logger logger = Logger.getLogger(AuthFilter.class.getName());

    public static final String TOKEN_KEY = AuthFilter.class.getName() + ":tokenKey";
    public static final String PATIENT_ID_KEY = AuthFilter.class.getName() + ":patientIdKey";

    @Override
    protected long getTokenId(ServletRequest request) throws UnauthorizedException {
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            Long tokenId = (Long) req.getSession().getAttribute(TOKEN_KEY);
            return tokenId.longValue();
        } catch (NullPointerException e) {
            throw new UnauthorizedException("invalid token id", e);
        }
    }

    @Override
    protected String getPatientId(ServletRequest request) throws UnauthorizedException {
        HttpServletRequest req = (HttpServletRequest) request;
        return (String) req.getSession().getAttribute(PATIENT_ID_KEY);
    }

    @Override
    protected void onUnauthorized(ServletResponse response) throws IOException {
        ((HttpServletResponse) response).sendRedirect("/web/login");
    }
}
