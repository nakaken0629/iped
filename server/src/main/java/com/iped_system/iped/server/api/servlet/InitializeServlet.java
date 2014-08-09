package com.iped_system.iped.server.api.servlet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by kenji on 2014/08/08.
 */
public class InitializeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Entity user = new Entity("User");
        user.setProperty("lastName", "中垣");
        user.setProperty("firstName", "健志");
        user.setProperty("userId", "nakaken0629@gmail.com");
        user.setProperty("password", "password");

        DatastoreService service = DatastoreServiceFactory.getDatastoreService();
        service.put(user);
    }
}
