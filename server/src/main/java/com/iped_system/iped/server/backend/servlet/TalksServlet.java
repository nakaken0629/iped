package com.iped_system.iped.server.backend.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.server.domain.UserDomain;
import com.iped_system.iped.server.domain.model.Talk;
import com.iped_system.iped.server.domain.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
        Query query = new Query("TalkValue");
        query.addSort("createdAt", Query.SortDirection.DESCENDING);
        PreparedQuery pq = service.prepare(query);
        ArrayList<Talk> talks = new ArrayList<Talk>();
        for (Entity user : pq.asIterable()) {
            Talk talkValue = new Talk();
            talkValue.setPatientId((String) user.getProperty("patientId"));
            String authorId = (String) user.getProperty("authorId");
            User author = domain.getByUserId(authorId);
            talkValue.setAuthorName(author.getLastName() + " " + author.getFirstName());
            talkValue.setCreatedAt((Date) user.getProperty("createdAt"));
            talkValue.setText((String) user.getProperty("text"));
            talks.add(talkValue);
        }
        req.setAttribute("talks", talks);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/talks.jsp");
        dispatcher.forward(req, resp);
    }
}
