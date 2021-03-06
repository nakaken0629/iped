package com.iped_system.iped.server.backend.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.Talk;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/10.
 */
public class TalksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* prepare talks */
        UserDomain domain = UserDomain.getInstance();
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Talk");
        query.addSort("createdAt", Query.SortDirection.DESCENDING);
        PreparedQuery pq = service.prepare(query);
        ArrayList<Talk> talks = new ArrayList<Talk>();
        for (Entity talk : pq.asIterable()) {
            Talk talkValue = new Talk(talk);
            talks.add(talkValue);
        }
        req.setAttribute("talks", talks);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/talks.jsp");
        dispatcher.forward(req, resp);
    }
}
