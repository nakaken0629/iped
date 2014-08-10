package com.iped_system.iped.server.backend.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.iped_system.iped.server.backend.model.Remark;

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
public class RemarksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* prepare remarks */
        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query("Remark");
        PreparedQuery pq = service.prepare(query);
        ArrayList<Remark> remarks = new ArrayList<Remark>();
        for(Entity userEntity : pq.asIterable()) {
            Remark remark = new Remark();
            remark.setAuthorId((String) userEntity.getProperty("authorName"));
            remark.setPatientId((String) userEntity.getProperty("patientId"));
            remark.setText((String) userEntity.getProperty("text"));
            remarks.add(remark);
        }
        req.setAttribute("remarks", remarks);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/backend/remarks.jsp");
        dispatcher.forward(req, resp);
    }
}
