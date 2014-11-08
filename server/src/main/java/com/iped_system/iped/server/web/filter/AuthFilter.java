package com.iped_system.iped.server.web.filter;

import com.iped_system.iped.server.common.filter.BaseAuthFilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/11/08.
 */
public class AuthFilter extends BaseAuthFilter {
    public static final String TOKEN_KEY = "token";
    @Override
    protected long getTokenId(ServletRequest request) throws UnauthorizedException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try {
            return (Long) httpServletRequest.getSession().getAttribute(TOKEN_KEY);
        } catch (NullPointerException e) {
            throw new UnauthorizedException("invalid token id", e);
        }
    }

    @Override
    protected void onUnauthorized(ServletResponse response) throws IOException {
        ((HttpServletResponse) response).sendRedirect("/web/login");
    }
}
